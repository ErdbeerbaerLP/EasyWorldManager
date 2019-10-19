package de.erdbeerbaerlp.worldManager;

public class ColorFormatter {
	/**
	 * Removes minecraft color-codes from any string
	 * @param formattedString Minecraft-Formatted String
	 * @return Plain string without any color code
	 */
	public static String unFormat(String formattedString){
		return formattedString.replaceAll("\u00A70", "").replaceAll("\u00A71", "").replaceAll("\u00A72", "").replaceAll("\u00A73", "").replaceAll("\u00A74", "").replaceAll("\u00A75", "").replaceAll("\u00A76", "").replaceAll("\u00A77", "").replaceAll("\u00A78", "").replaceAll("\u00A79", "").replaceAll("\u00A7a", "").replaceAll("\u00A7b", "").replaceAll("\u00A7c", "").replaceAll("\u00A7d", "").replaceAll("\u00A7e", "").replaceAll("\u00A7f", "").replaceAll("\u00A7l", "").replaceAll("\u00A7k", "").replaceAll("\u00A7m", "").replaceAll("\u00A7n", "").replaceAll("\u00A7o", "").replaceAll("\u00A7r", "");
	}
	
	/**
	 * Formats Minecraft-Formatted Strings to HTML
	 * @param stringToFormat String that will be formatted from Minecraft to HTML. The String should already use <html> tags
	 * @return HTML-Formatted String
	 */
	public static String format(String stringToFormat) {
		stringToFormat = stringToFormat.replace("\u00A70", "<span style=\"color: #000000;font-style: normal;text-decoration: none;font-weight: normal;\">");
		stringToFormat = stringToFormat.replace("\u00A71", "<span style=\"color: #0000AA;font-style: normal;text-decoration: none;font-weight: normal;\">");
		stringToFormat = stringToFormat.replace("\u00A72", "<span style=\"color: #00AA00;font-style: normal;text-decoration: none;font-weight: normal;\">");
		stringToFormat = stringToFormat.replace("\u00A73", "<span style=\"color: #00AAAA;font-style: normal;text-decoration: none;font-weight: normal;\">");
		stringToFormat = stringToFormat.replace("\u00A74", "<span style=\"color: #AA0000;font-style: normal;text-decoration: none;font-weight: normal;\">");
		stringToFormat = stringToFormat.replace("\u00A75", "<span style=\"color: #AA00AA;font-style: normal;text-decoration: none;font-weight: normal;\">");
		stringToFormat = stringToFormat.replace("\u00A76", "<span style=\"color: #FFAA00;font-style: normal;text-decoration: none;font-weight: normal;\">");
		stringToFormat = stringToFormat.replace("\u00A77", "<span style=\"color: #AAAAAA;font-style: normal;text-decoration: none;font-weight: normal;\">");
		stringToFormat = stringToFormat.replace("\u00A78", "<span style=\"color: #555555;font-style: normal;text-decoration: none;font-weight: normal;\">");
		stringToFormat = stringToFormat.replace("\u00A79", "<span style=\"color: #5555FF;font-style: normal;text-decoration: none;font-weight: normal;\">");
		stringToFormat = stringToFormat.replace("\u00A7A", "<span style=\"color: #55FF55;font-style: normal;text-decoration: none;font-weight: normal;\">");
		stringToFormat = stringToFormat.replace("\u00A7B", "<span style=\"color: #55FFFF;font-style: normal;text-decoration: none;font-weight: normal;\">");
		stringToFormat = stringToFormat.replace("\u00A7C", "<span style=\"color: #FF5555;font-style: normal;text-decoration: none;font-weight: normal;\">");
		stringToFormat = stringToFormat.replace("\u00A7D", "<span style=\"color: #cece00;font-style: normal;text-decoration: none;font-weight: normal;\">");
		stringToFormat = stringToFormat.replace("\u00A7E", "<span style=\"color: #FFFF55;font-style: normal;text-decoration: none;font-weight: normal;\">");
		stringToFormat = stringToFormat.replace("\u00A7F", "<span style=\"color: #FFFFFF;font-style: normal;text-decoration: none;font-weight: normal;\">");
		stringToFormat = stringToFormat.replace("\u00A7a", "<span style=\"color: #55FF55;font-style: normal;text-decoration: none;font-weight: normal;\">");
		stringToFormat = stringToFormat.replace("\u00A7b", "<span style=\"color: #55FFFF;font-style: normal;text-decoration: none;font-weight: normal;\">");
		stringToFormat = stringToFormat.replace("\u00A7c", "<span style=\"color: #FF5555;font-style: normal;text-decoration: none;font-weight: normal;\">");
		stringToFormat = stringToFormat.replace("\u00A7d", "<span style=\"color: #FF55FF;font-style: normal;text-decoration: none;font-weight: normal;\">");
		stringToFormat = stringToFormat.replace("\u00A7e", "<span style=\"color: #cece00;font-style: normal;text-decoration: none;font-weight: normal;\">");
		stringToFormat = stringToFormat.replace("\u00A7f", "<span style=\"color: #FFFFFF;font-style: normal;text-decoration: none;font-weight: normal;\">");
		
		stringToFormat = stringToFormat.replace("\u00A7N", "<span style=\"text-decoration: underline;\">");
		stringToFormat = stringToFormat.replace("\u00A7n", "<span style=\"text-decoration: underline;\">");
		
		stringToFormat = stringToFormat.replace("\u00A7M", "<span style=\"text-decoration: line-through;\">");
		stringToFormat = stringToFormat.replace("\u00A7m", "<span style=\"text-decoration: line-through;\">");
		
		stringToFormat = stringToFormat.replace("\u00A7L", "<span style=\"font-weight: bold;\">");
		stringToFormat = stringToFormat.replace("\u00A7l", "<span style=\"font-weight: bold;\">");		
		
		stringToFormat = stringToFormat.replace("\u00A7K", "");
		stringToFormat = stringToFormat.replace("\u00A7k", "");		
		
		stringToFormat = stringToFormat.replace("\u00A7O", "<span style=\"font-style: italic;\">");
		stringToFormat = stringToFormat.replace("\u00A7o", "<span style=\"font-style: italic;\">");		
		
		
		
		stringToFormat = stringToFormat.replace("\u00A7r", "<span style=\"color: #000000;text-decoration: none;font-style: normal;font-weight: normal;\">");
		
		stringToFormat = stringToFormat.replace("\u00A7", "");
		return stringToFormat+"<span style=\"color: #000000;text-decoration: none;font-style: normal;font-weight: normal;\">";
	}
	/**
	 * Formats Minecraft-Formatted Strings to HTML
	 * @param stringToFormat String that will be formatted from Minecraft to HTML
	 * @param addHtmlTags set to true to add <html> and </html> to the string
	 * @return Minecraft-Formatted String
	 */
	public static String format(String stringToFormat, boolean addHtmlTags) {
		if(addHtmlTags == false) return format(stringToFormat);
		else return format("<html>"+stringToFormat+"</html>");
	}

}
