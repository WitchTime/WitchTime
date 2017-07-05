/* 
  This file is part of AvatarMod.
    
  AvatarMod is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.
  
  AvatarMod is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.
  
  You should have received a copy of the GNU General Public License
  along with AvatarMod. If not, see <http://www.gnu.org/licenses/>.
*/
package com.crowsofwar.gorecore.format;

import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.crowsofwar.gorecore.GoreCore;
import com.crowsofwar.gorecore.format.ChatSender.ProcessingException;
import com.crowsofwar.gorecore.format.FormattingState.ChatFormatSet;
import com.crowsofwar.gorecore.format.FormattingState.Setting;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class FormattedMessageProcessor {
	
	/**
	 * Formats the chat message to apply colors and translation arguments.
	 * 
	 * @param msg
	 *            Information about how to format the message
	 * @param text
	 *            Text to format
	 * @param formatValues
	 *            Values of the formatting arguments to use
	 */
	public static String formatText(FormattedMessage msg, String text, Object... formatValues) {
		
		MessageConfiguration cfg = msg.getConfig();
		ChatFormatSet formatSet = new ChatFormatSet();
		
		for (Map.Entry<String, TextFormatting> color : cfg.allColors().entrySet()) {
			formatSet.addFormat(color.getKey(), color.getValue(), Setting.UNKNOWN, Setting.UNKNOWN);
		}
		
		String[] translateArgs = msg.getTranslationArgs();
		for (int i = 0; i < translateArgs.length; i++) {
			text = text.replace("${" + translateArgs[i] + "}", formatValues[i].toString());
		}
		
		Set<Map.Entry<String, String>> consts = cfg.getAllConstants();
		for (Map.Entry<String, String> entry : consts) {
			text = text.replace("${" + entry.getKey() + "}", entry.getValue());
		}
		
		FormattingState format = new FormattingState();
		
		String newText = "";
		
		// Separate the text by square brackets
		// for demo, see http://regexr.com/, regex is: \\?\[?\/?[^\]\[\\]+\]?
		Matcher matcher = Pattern.compile("\\\\?\\[?\\/?[^\\]\\[\\\\]+\\]?").matcher(text);
		
		while (matcher.find()) {
			
			// Item may be a tag, may not be
			String item = matcher.group();
			
			boolean recievedFormatInstruction = false;
			
			if (item.equals("")) continue;
			
			if (item.startsWith("[") && item.endsWith("]")) {
				
				// Is a tag
				
				String tag = item.substring(1, item.length() - 1);
				recievedFormatInstruction = true;
				
				if (formatSet.isFormatFor(tag)) {
					
					format.pushFormat(formatSet.lookup(tag));
					recievedFormatInstruction = true;
					
				} else if (tag.startsWith("/")) {
					
					if (tag.substring(1).equals(format.topFormat().getName())) {
						
						format.popFormat();
						
					} else {
						throw new ProcessingException(
								"Error processing message; closing tag does not match last opened tag: "
										+ text);
					}
					
				} else if (tag.startsWith("translate=")) {
					
					String key = tag.substring("translate=".length());
					item = formatText(msg, I18n.format(key), formatValues);
					recievedFormatInstruction = false;
					
				} else if (tag.startsWith("keybinding=")) {
					
					String key = tag.substring("keybinding=".length());
					item = GoreCore.proxy.getKeybindingDisplayName(key);
					recievedFormatInstruction = false;
					
				} else {
					
					throw new ProcessingException("String has invalid tag: [" + item + "]; text is " + text);
					
				}
				
			}
			// remove backslash from escaped tags
			if (item.startsWith("\\")) item = item.substring(1);
			
			// If any formats changed, must re add all chat formats
			if (recievedFormatInstruction) {
				newText += TextFormatting.RESET;
				newText += format.getColor(); // For some reason, color must
												// come before bold
				newText += format.isBold() ? TextFormatting.BOLD : "";
				newText += format.isItalic() ? TextFormatting.ITALIC : "";
			} else {
				newText += item;
			}
			
		}
		
		if (format.hasFormat()) {
			throw new ProcessingException(
					"Unclosed tag [" + format.topFormat().getName() + "] in text " + text);
		}
		
		return newText;
		
	}
	
}