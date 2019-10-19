package de.erdbeerbaerlp.worldManager;

import static com.therandomlabs.utils.logging.Logging.getLogger;

import java.awt.Dimension;
import java.awt.Image;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.Set;

import javax.swing.ImageIcon;

import org.json.JSONObject;

import com.therandomlabs.curseapi.CurseForgeSite;
import com.therandomlabs.curseapi.file.CurseFile;
import com.therandomlabs.curseapi.project.CurseProject;
import com.therandomlabs.curseapi.project.Member;
import com.therandomlabs.curseapi.project.MemberType;
import com.therandomlabs.utils.collection.TRLList;

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
	public TRLList<Member> authors;
	private String slug;
	public CurseFile newestFile;
	
	public Mod(String mid, String ver, JSONObject json) {
		this.version = ver;
		if(this.version.isEmpty()) this.version = "<span style=\"color: #FF0B0B;\">Unknown</span>";
		this.modid = mid;
		this.slug = this.modid;
		try {
			if(!json.isEmpty()) {
			final Set<String> keySet = json.keySet();
		    for(String key : keySet) {
		    	if(this.modid.equals(key)) {
		    		this.slug = (String) json.get(key);
		    		
		    		if(this.slug.isEmpty()) {
		    			this.exclude = true;
		    			break;
		    		}
		    		getLogger().debug("Redirecting from "+this.modid+" to "+this.slug);
		    		break;
		    	}
		    }
			}
			final CurseProject p = CurseProject.fromSlug(CurseForgeSite.MINECRAFT, this.slug);
			if(p.site() == CurseForgeSite.MINECRAFT) {
			this.name = p.title();
			this.id = p.id();
			this.owner = p.members(MemberType.OWNER).get(0).username();
			this.authors = p.members();
			this.lastUpdated = p.lastUpdateTime();
			this.creationTime = p.creationTime();
			this.shortDescription = p.shortDescription();
			this.newestFile = p.latestFile();
			this.fullDescription = p.descriptionHTML().toString();
			this.downloads = p.downloads();
			
			if(p.thumbnail().getHeight() != p.thumbnail().getWidth()) {
				final Dimension test = getScaledDimension(new Dimension(p.thumbnail().getWidth(), p.thumbnail().getHeight()), new Dimension(65,65));
				this.icon = new ImageIcon(p.thumbnail().getScaledInstance(test.width, test.height, Image.SCALE_SMOOTH));
			}else {
				this.icon = new ImageIcon(p.thumbnail().getScaledInstance(64, 64, Image.SCALE_SMOOTH));
			}
			this.uri = p.url().toURI();
			this.complete = true;
			}else {
				if(this.modid.toLowerCase().equals("forge")) {
					this.icon = new ImageIcon(new ImageIcon(mainWindow.class.getResource("/de/erdbeerbaerlp/worldManager/icons/forge.png")).getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH));
					this.uri = new URL("http://files.minecraftforge.net").toURI();
					this.name = "Minecraft Forge";
					this.id = -1;
					this.lastUpdated = null;
					this.creationTime = null;
					this.downloads = -1;
					this.shortDescription = "Minecraft Forge is minecraft´s modding API.";
					this.fullDescription = "Minecraft Forge is minecraft´s modding API.<br>Without Forge you will be unable to install any mods to Minecraft!";
					this.owner = "Many";
					TRLList<Member> authors = new TRLList<Member>();
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
					
					
					this.authors = authors;
					this.complete = true;
				}else {
				System.out.println("INVALID CURSE SITE \""+p.game()+"\"!");
				throw new Exception();
				}
			}
			
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			try {
				this.uri = new URL("https://www.google.de/search?q=minecraft+mod+"+this.modid.replace("|", "%7C").replace(" ", "+")+"+"+this.version).toURI();
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (URISyntaxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			this.complete = false;
		}
		
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
		// TODO Auto-generated method stub
		if(this.modid.equals("forge")) return "1";
		else return this.complete ? this.name:this.modid;
	}
	@Override
	public int compareTo(Mod o) {
		return this.toString().compareTo(o.toString());
	}
}
