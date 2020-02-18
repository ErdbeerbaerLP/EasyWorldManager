package de.erdbeerbaerlp.worldManager;

import com.therandomlabs.curseapi.CurseAPI;
import com.therandomlabs.curseapi.file.CurseFile;
import com.therandomlabs.curseapi.project.CurseMember;
import com.therandomlabs.curseapi.project.CurseProject;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

public class Mod implements Comparable<Mod>{
	public String version;
	public String modid;
	
	public String name;
	public String shortDescription;
	public String fullDescription;
	public ZonedDateTime creationTime;
	public ZonedDateTime lastUpdated;
	public int id;
	public int downloads;
	public boolean complete = false;
	public ImageIcon icon = new ImageIcon(new ImageIcon(mainWindow.class.getResource("/de/erdbeerbaerlp/worldManager/icons/unknown.png")).getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH));
	public URI uri;
	public boolean exclude = false;
	public String owner;
	public ArrayList<CurseMember> authors = new ArrayList<>();
	private String slug;
	public CurseFile newestFile;

	public Mod(String mid, String ver, JSONObject json, boolean useCF) {
		this.version = ver;
		if (this.version.isEmpty()) this.version = "<span style=\"color: #FF0B0B;\">Unknown</span>";
		this.modid = mid;
		this.slug = this.modid;
		if (useCF) try {
			if (!json.isEmpty()) {
				final Set<String> keySet = json.keySet();
				for (String key : keySet) {
					if (this.modid.equals(key)) {
						this.slug = (String) json.get(key);

						if (this.slug.isEmpty()) {
							this.exclude = true;
							break;
						}
						System.out.println("Redirecting from " + this.modid + " to " + this.slug);
						break;
					}
				}
			}
			if (this.slug.toLowerCase().equals("forge")) {
				this.icon = new ImageIcon(new ImageIcon(mainWindow.class.getResource("/de/erdbeerbaerlp/worldManager/icons/forge.png")).getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH));
				this.uri = new URL("http://files.minecraftforge.net").toURI();
				this.name = "Minecraft Forge";
				this.id = -1;
				this.lastUpdated = null;
				this.creationTime = null;
				this.downloads = -1;
				this.shortDescription = "Minecraft Forge is minecraft's modding API.";
				this.fullDescription = "Minecraft Forge is minecraft's modding API.<br>Without Forge you will be unable to install any mods to Minecraft!";
				this.owner = "Many";
				/*
					ArrayList<CurseMember> authors = new ArrayList<CurseMember>();
					Constructor<CurseMember> constructor;
			        constructor = CurseMember.class.getDeclaredConstructor();
			        constructor.setAccessible(true);
					authors.add(constructor.newInstance(CurseMember.AUTHOR, "Searge"));
					authors.add(constructor.newInstance(CurseMember.AUTHOR, "ProfMobius"));
					authors.add(constructor.newInstance(CurseMember.AUTHOR, "IngisKahn"));
					authors.add(constructor.newInstance(CurseMember.AUTHOR, "Fesh0r"));
					authors.add(constructor.newInstance(MemberType.AUTHOR, "ZeuX"));
					authors.add(constructor.newInstance(MemberType.AUTHOR, "R4wk"));
					authors.add(constructor.newInstance(MemberType.AUTHOR, "LexManos"));
					authors.add(constructor.newInstance(MemberType.AUTHOR, "Bspkrs"));
					authors.add(constructor.newInstance(MemberType.CONTRIBUTOR, "Others"));


				this.authors = authors;*/
				this.complete = true;
				return;
			}//else {
			final Optional<CurseProject> search = CurseAPI.projectByURL("/minecraft/mc-mods/" + slug);
			if (!search.isPresent()) throw new Exception("Not Found");
			CurseProject p = search.get();
			if (p == null) throw new Exception("Not Found");
			this.name = p.name();
			this.id = p.id();
			this.owner = p.author().name();
			authors.addAll(p.authors());
			this.lastUpdated = p.lastUpdateTime();
			this.creationTime = p.creationTime();
			this.shortDescription = p.summary();
			this.newestFile = p.files().last();
			this.fullDescription = p.description().html();
			this.downloads = p.downloadCount();

			if (p.logo().get().getHeight() != p.logo().get().getWidth()) {
				final Dimension test = getScaledDimension(new Dimension(p.logo().get().getWidth(), p.logo().get().getHeight()), new Dimension(65, 65));
				this.icon = new ImageIcon(p.logo().get().getScaledInstance(test.width, test.height, Image.SCALE_SMOOTH));
			} else {
				this.icon = new ImageIcon(p.logo().get().getScaledInstance(64, 64, Image.SCALE_SMOOTH));
			}
			this.uri = p.url().uri();
			this.complete = true;

			//System.out.println("INVALID CURSE SITE \""+p.game()+"\"!");
			//throw new Exception();
			//}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			try {
				this.uri = new URL("https://www.google.de/search?q=minecraft+mod+" + URLEncoder.encode(this.modid + " " + this.version)).toURI();
			} catch (MalformedURLException | URISyntaxException e1) {
				e1.printStackTrace();
			}
			this.complete = false;
		}
		
	}
	
	public Mod(String mid, String ver, JSONObject json) {
		this(mid, ver, json, true);
	}
	
	private Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {
		
		int original_width = imgSize.width;
		int original_height = imgSize.height;
		int bound_width = boundary.width;
		int bound_height = boundary.height;
		int new_width = original_width;
		int new_height = original_height;

	    // first check if we need to scale width
	    if (original_width > bound_width) {
	        //scale width to fit
	        new_width = bound_width;
	        //scale height to maintain aspect ratio
	        new_height = (new_width * original_height) / original_width;
	    }

	    // then check if we need to scale even with the new height
	    if (new_height > bound_height) {
	        //scale height to fit instead
	        new_height = bound_height;
	        //scale width to maintain aspect ratio
			new_width = (new_height * original_width) / original_height;
		}
		
		return new Dimension(new_width, new_height);
	}
	
	@Override
	public String toString() {
		if (this.modid.equals("forge")) return "1";
		else return this.complete ? this.name : this.modid;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Mod) {
			return ((Mod) obj).modid.equals(modid);
		}
		return false;
	}
	
	@Override
	public int compareTo(Mod o) {
		return this.toString().compareTo(o.toString());
	}
}
