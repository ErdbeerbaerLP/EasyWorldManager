package de.erdbeerbaerlp.worldManager;

import com.therandomlabs.curseapi.CurseAPI;
import com.therandomlabs.curseapi.file.CurseFile;
import com.therandomlabs.curseapi.minecraft.CurseAPIMinecraft;
import com.therandomlabs.curseapi.project.CurseMember;
import com.therandomlabs.curseapi.project.CurseProject;
import com.therandomlabs.curseapi.project.CurseSearchQuery;
import com.therandomlabs.curseapi.project.CurseSearchSort;
import com.therandomlabs.utils.collection.TRLList;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.therandomlabs.utils.logging.Logging.getLogger;

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
	public TRLList<CurseMember> authors;
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
						getLogger().debug("Redirecting from "+this.modid+" to "+this.slug);
						break;
					}
				}
			}
			final Optional<List<CurseProject>> search = CurseAPI.searchProjects(new CurseSearchQuery().gameID(CurseAPIMinecraft.MINECRAFT_ID).searchFilter(this.slug).sortingMethod(CurseSearchSort.NAME).pageSize(100));
			if (!search.isPresent()) throw new Exception("Not Found");
			CurseProject p = null;
			//Parse projects by slug
			for (final CurseProject pr : search.get()) {
				if (pr.slug().equals(this.slug)) {
					p = pr;
				}
			}
			if (p == null) throw new Exception("Not Found");
			this.name = p.name();
			this.id = p.id();
			this.owner = p.author().name();
			final TRLList<CurseMember> curseMembers = new TRLList<>();
			curseMembers.addAll(p.authors());
			this.authors = curseMembers;
			this.lastUpdated = p.lastUpdateTime();
			this.creationTime = p.creationTime();
			this.shortDescription = p.summary();
			this.newestFile = p.files().last();
			this.fullDescription = p.description().html();
			this.downloads = p.downloadCount();
			
			if (p.avatarThumbnail().getHeight() != p.avatarThumbnail().getWidth()) {
				final Dimension test = getScaledDimension(new Dimension(p.avatarThumbnail().getWidth(), p.avatarThumbnail().getHeight()), new Dimension(65, 65));
				this.icon = new ImageIcon(p.avatarThumbnail().getScaledInstance(test.width, test.height, Image.SCALE_SMOOTH));
			}else {
				this.icon = new ImageIcon(p.avatarThumbnail().getScaledInstance(64, 64, Image.SCALE_SMOOTH));
			}
			this.uri = p.url().uri();
			this.complete = true;
			if(this.modid.toLowerCase().equals("forge")) {
				this.icon = new ImageIcon(new ImageIcon(mainWindow.class.getResource("/de/erdbeerbaerlp/worldManager/icons/forge.png")).getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH));
				this.uri = new URL("http://files.minecraftforge.net").toURI();
				this.name = "Minecraft Forge";
				this.id = -1;
				this.lastUpdated = null;
				this.creationTime = null;
				this.downloads = -1;
				this.shortDescription = "Minecraft Forge is minecraft�s modding API.";
				this.fullDescription = "Minecraft Forge is minecraft�s modding API.<br>Without Forge you will be unable to install any mods to Minecraft!";
				this.owner = "Many";
					/*TRLList<CurseMember> authors = new TRLList<Member>();
					Constructor<Member> constructor;
			        constructor = Member.class.getDeclaredConstructor(MemberType.class, String.class);
			        constructor.setAccessible(true);
					authors.add(constructor.newInstance(MemberType.AUTHOR, "Searge"));
					authors.add(constructor.newInstance(MemberType.AUTHOR, "ProfMobius"));
					authors.add(constructor.newInstance(MemberType.AUTHOR, "IngisKahn"));
					authors.add(constructor.newInstance(MemberType.AUTHOR, "Fesh0r"));
					authors.add(constructor.newInstance(MemberType.AUTHOR, "ZeuX"));
					authors.add(constructor.newInstance(MemberType.AUTHOR, "R4wk"));
					authors.add(constructor.newInstance(MemberType.AUTHOR, "LexManos"));
					authors.add(constructor.newInstance(MemberType.AUTHOR, "Bspkrs"));
					authors.add(constructor.newInstance(MemberType.CONTRIBUTOR, "Others"));
					*/
				
				this.authors = authors;
				this.complete = true;
			}//else {
			//System.out.println("INVALID CURSE SITE \""+p.game()+"\"!");
			//throw new Exception();
			//}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			try {
				this.uri = new URL("https://www.google.de/search?q=minecraft+mod+" + this.modid.replace("|", "%7C").replace(" ", "+") + "+" + this.version).toURI();
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
