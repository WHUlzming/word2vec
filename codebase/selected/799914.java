package org.apache.roller.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * General purpose utilities.
 *
 * <pre>
 * Includes TextToHTML methods donated by Erik Thauvin
 * Copyright (C) 2002-2003 by Erik C. Thauvin (erik@thauvin.net).
 * All rights reserved.
 * </pre>
 * 
 * <pre>
 * Also includes addNoFollow() and transformToHTMLSubset() from 
 * Simon Brown's Pebble blog server (BSD license).
 * Copyright (c) 2003-2005, Simon Brown
 * All rights reserved.
 * </pre>
 *
 * @author David M Johnson
 * @author Lance Lavandowska
 * @author Matt Raible (added encryption methods)
 */
public class Utilities {

    /** The <code>Log</code> instance for this class. */
    private static Log mLogger = LogFactory.getLog(Utilities.class);

    /** Pattern for matching HTML links */
    private static Pattern mLinkPattern = Pattern.compile("<a href=.*?>", Pattern.CASE_INSENSITIVE);

    private static final Pattern OPENING_B_TAG_PATTERN = Pattern.compile("&lt;b&gt;", Pattern.CASE_INSENSITIVE);

    private static final Pattern CLOSING_B_TAG_PATTERN = Pattern.compile("&lt;/b&gt;", Pattern.CASE_INSENSITIVE);

    private static final Pattern OPENING_I_TAG_PATTERN = Pattern.compile("&lt;i&gt;", Pattern.CASE_INSENSITIVE);

    private static final Pattern CLOSING_I_TAG_PATTERN = Pattern.compile("&lt;/i&gt;", Pattern.CASE_INSENSITIVE);

    private static final Pattern OPENING_BLOCKQUOTE_TAG_PATTERN = Pattern.compile("&lt;blockquote&gt;", Pattern.CASE_INSENSITIVE);

    private static final Pattern CLOSING_BLOCKQUOTE_TAG_PATTERN = Pattern.compile("&lt;/blockquote&gt;", Pattern.CASE_INSENSITIVE);

    private static final Pattern BR_TAG_PATTERN = Pattern.compile("&lt;br */*&gt;", Pattern.CASE_INSENSITIVE);

    private static final Pattern OPENING_P_TAG_PATTERN = Pattern.compile("&lt;p&gt;", Pattern.CASE_INSENSITIVE);

    private static final Pattern CLOSING_P_TAG_PATTERN = Pattern.compile("&lt;/p&gt;", Pattern.CASE_INSENSITIVE);

    private static final Pattern OPENING_PRE_TAG_PATTERN = Pattern.compile("&lt;pre&gt;", Pattern.CASE_INSENSITIVE);

    private static final Pattern CLOSING_PRE_TAG_PATTERN = Pattern.compile("&lt;/pre&gt;", Pattern.CASE_INSENSITIVE);

    private static final Pattern OPENING_UL_TAG_PATTERN = Pattern.compile("&lt;ul&gt;", Pattern.CASE_INSENSITIVE);

    private static final Pattern CLOSING_UL_TAG_PATTERN = Pattern.compile("&lt;/ul&gt;", Pattern.CASE_INSENSITIVE);

    private static final Pattern OPENING_OL_TAG_PATTERN = Pattern.compile("&lt;ol&gt;", Pattern.CASE_INSENSITIVE);

    private static final Pattern CLOSING_OL_TAG_PATTERN = Pattern.compile("&lt;/ol&gt;", Pattern.CASE_INSENSITIVE);

    private static final Pattern OPENING_LI_TAG_PATTERN = Pattern.compile("&lt;li&gt;", Pattern.CASE_INSENSITIVE);

    private static final Pattern CLOSING_LI_TAG_PATTERN = Pattern.compile("&lt;/li&gt;", Pattern.CASE_INSENSITIVE);

    private static final Pattern CLOSING_A_TAG_PATTERN = Pattern.compile("&lt;/a&gt;", Pattern.CASE_INSENSITIVE);

    private static final Pattern OPENING_A_TAG_PATTERN = Pattern.compile("&lt;a href=.*?&gt;", Pattern.CASE_INSENSITIVE);

    private static final Pattern QUOTE_PATTERN = Pattern.compile("&quot;", Pattern.CASE_INSENSITIVE);

    /**
     * Test if string is not null and and not empty.
     */
    public static boolean isNotEmpty(String str) {
        return StringUtils.isNotEmpty(str);
    }

    /**
     * Test if string is null or empty.
     */
    public static boolean isEmpty(String str) {
        return StringUtils.isEmpty(str);
    }

    /** Strip jsessionid off of a URL */
    public static String stripJsessionId(String url) {
        int startPos = url.indexOf(";jsessionid=");
        if (startPos != -1) {
            int endPos = url.indexOf("?", startPos);
            if (endPos == -1) {
                url = url.substring(0, startPos);
            } else {
                url = url.substring(0, startPos) + url.substring(endPos, url.length());
            }
        }
        return url;
    }

    /**
     * Escape, but do not replace HTML.
     * The default behaviour is to escape ampersands.
     */
    public static String escapeHTML(String s) {
        return escapeHTML(s, true);
    }

    /**
     * Escape, but do not replace HTML.
     * @param escapeAmpersand Optionally escape
     * ampersands (&amp;).
     */
    public static String escapeHTML(String s, boolean escapeAmpersand) {
        if (escapeAmpersand) {
            s = stringReplace(s, "&", "&amp;");
        }
        s = stringReplace(s, "&nbsp;", " ");
        s = stringReplace(s, "\"", "&quot;");
        s = stringReplace(s, "<", "&lt;");
        s = stringReplace(s, ">", "&gt;");
        return s;
    }

    public static String unescapeHTML(String str) {
        return StringEscapeUtils.unescapeHtml(str);
    }

    /**
     * Remove occurences of html, defined as any text
     * between the characters "&lt;" and "&gt;".  Replace
     * any HTML tags with a space.
     */
    public static String removeHTML(String str) {
        return removeHTML(str, true);
    }

    /**
     * Remove occurences of html, defined as any text
     * between the characters "&lt;" and "&gt;".
     * Optionally replace HTML tags with a space.
     *
     * @param str
     * @param addSpace
     * @return
     */
    public static String removeHTML(String str, boolean addSpace) {
        if (str == null) return "";
        StringBuffer ret = new StringBuffer(str.length());
        int start = 0;
        int beginTag = str.indexOf("<");
        int endTag = 0;
        if (beginTag == -1) return str;
        while (beginTag >= start) {
            if (beginTag > 0) {
                ret.append(str.substring(start, beginTag));
                if (addSpace) ret.append(" ");
            }
            endTag = str.indexOf(">", beginTag);
            if (endTag > -1) {
                start = endTag + 1;
                beginTag = str.indexOf("<", start);
            } else {
                ret.append(str.substring(beginTag));
                break;
            }
        }
        if (endTag > -1 && endTag + 1 < str.length()) {
            ret.append(str.substring(endTag + 1));
        }
        return ret.toString().trim();
    }

    /** Run both removeHTML and escapeHTML on a string.
     * @param s String to be run through removeHTML and escapeHTML.
     * @return String with HTML removed and HTML special characters escaped.
     */
    public static String removeAndEscapeHTML(String s) {
        if (s == null) return ""; else return Utilities.escapeHTML(Utilities.removeHTML(s));
    }

    /**
     * Autoformat.
     */
    public static String autoformat(String s) {
        String ret = StringUtils.replace(s, "\n", "<br />");
        return ret;
    }

    /**
     * Format date in ISO-8601 format.
     */
    public static String formatIso8601Date(Date d) {
        return DateUtil.formatIso8601(d);
    }

    /**
     * Format date in ISO-8601 format.
     */
    public static String formatIso8601Day(Date d) {
        return DateUtil.formatIso8601Day(d);
    }

    /**
     * Return a date in RFC-822 format.
     */
    public static String formatRfc822Date(Date date) {
        return DateUtil.formatRfc822(date);
    }

    /**
     * Return a date in RFC-822 format.
     */
    public static String format8charsDate(Date date) {
        return DateUtil.format8chars(date);
    }

    /**
     * Replaces occurences of non-alphanumeric characters with an underscore.
     */
    public static String replaceNonAlphanumeric(String str) {
        return replaceNonAlphanumeric(str, '_');
    }

    /**
     * Replaces occurences of non-alphanumeric characters with a
     * supplied char.
     */
    public static String replaceNonAlphanumeric(String str, char subst) {
        StringBuffer ret = new StringBuffer(str.length());
        char[] testChars = str.toCharArray();
        for (int i = 0; i < testChars.length; i++) {
            if (Character.isLetterOrDigit(testChars[i])) {
                ret.append(testChars[i]);
            } else {
                ret.append(subst);
            }
        }
        return ret.toString();
    }

    /**
     * Remove occurences of non-alphanumeric characters.
     */
    public static String removeNonAlphanumeric(String str) {
        StringBuffer ret = new StringBuffer(str.length());
        char[] testChars = str.toCharArray();
        for (int i = 0; i < testChars.length; i++) {
            if (Character.isLetterOrDigit(testChars[i]) || testChars[i] == '.') {
                ret.append(testChars[i]);
            }
        }
        return ret.toString();
    }

    /**
     * @param stringArray
     * @param delim
     * @return
     */
    public static String stringArrayToString(String[] stringArray, String delim) {
        String ret = "";
        for (int i = 0; i < stringArray.length; i++) {
            if (ret.length() > 0) ret = ret + delim + stringArray[i]; else ret = stringArray[i];
        }
        return ret;
    }

    /**
     * Replace occurrences of str1 in string str with str2
     */
    public static String stringReplace(String str, String str1, String str2) {
        String ret = StringUtils.replace(str, str1, str2);
        return ret;
    }

    /**
     * Replace occurrences of str1 in string str with str2
     * @param str String to operate on
     * @param str1 String to be replaced
     * @param str2 String to be used as replacement
     * @param maxCount Number of times to replace, 0 for all
     */
    public static String stringReplace(String str, String str1, String str2, int maxCount) {
        String ret = StringUtils.replace(str, str1, str2, maxCount);
        return ret;
    }

    /** Convert string to string array. */
    public static String[] stringToStringArray(String instr, String delim) throws NoSuchElementException, NumberFormatException {
        StringTokenizer toker = new StringTokenizer(instr, delim);
        String stringArray[] = new String[toker.countTokens()];
        int i = 0;
        while (toker.hasMoreTokens()) {
            stringArray[i++] = toker.nextToken();
        }
        return stringArray;
    }

    /** Convert string to integer array. */
    public static int[] stringToIntArray(String instr, String delim) throws NoSuchElementException, NumberFormatException {
        StringTokenizer toker = new StringTokenizer(instr, delim);
        int intArray[] = new int[toker.countTokens()];
        int i = 0;
        while (toker.hasMoreTokens()) {
            String sInt = toker.nextToken();
            int nInt = Integer.parseInt(sInt);
            intArray[i++] = new Integer(nInt).intValue();
        }
        return intArray;
    }

    /** Convert integer array to a string. */
    public static String intArrayToString(int[] intArray) {
        String ret = "";
        for (int i = 0; i < intArray.length; i++) {
            if (ret.length() > 0) ret = ret + "," + Integer.toString(intArray[i]); else ret = Integer.toString(intArray[i]);
        }
        return ret;
    }

    public static void copyFile(File from, File to) throws IOException {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(from);
        } catch (IOException ex) {
            throw new IOException("Utilities.copyFile: opening input stream '" + from.getPath() + "', " + ex.getMessage());
        }
        try {
            out = new FileOutputStream(to);
        } catch (Exception ex) {
            try {
                in.close();
            } catch (IOException ex1) {
            }
            throw new IOException("Utilities.copyFile: opening output stream '" + to.getPath() + "', " + ex.getMessage());
        }
        copyInputToOutput(in, out, from.length());
    }

    /**
     * Utility method to copy an input stream to an output stream.
     * Wraps both streams in buffers. Ensures right numbers of bytes copied.
     */
    public static void copyInputToOutput(InputStream input, OutputStream output, long byteCount) throws IOException {
        int bytes;
        long length;
        BufferedInputStream in = new BufferedInputStream(input);
        BufferedOutputStream out = new BufferedOutputStream(output);
        byte[] buffer;
        buffer = new byte[8192];
        for (length = byteCount; length > 0; ) {
            bytes = (int) (length > 8192 ? 8192 : length);
            try {
                bytes = in.read(buffer, 0, bytes);
            } catch (IOException ex) {
                try {
                    in.close();
                    out.close();
                } catch (IOException ex1) {
                }
                throw new IOException("Reading input stream, " + ex.getMessage());
            }
            if (bytes < 0) break;
            length -= bytes;
            try {
                out.write(buffer, 0, bytes);
            } catch (IOException ex) {
                try {
                    in.close();
                    out.close();
                } catch (IOException ex1) {
                }
                throw new IOException("Writing output stream, " + ex.getMessage());
            }
        }
        try {
            in.close();
            out.close();
        } catch (IOException ex) {
            throw new IOException("Closing file streams, " + ex.getMessage());
        }
    }

    public static void copyInputToOutput(InputStream input, OutputStream output) throws IOException {
        BufferedInputStream in = new BufferedInputStream(input);
        BufferedOutputStream out = new BufferedOutputStream(output);
        byte buffer[] = new byte[8192];
        for (int count = 0; count != -1; ) {
            count = in.read(buffer, 0, 8192);
            if (count != -1) out.write(buffer, 0, count);
        }
        try {
            in.close();
            out.close();
        } catch (IOException ex) {
            throw new IOException("Closing file streams, " + ex.getMessage());
        }
    }

    /**
     * Encode a string using algorithm specified in web.xml and return the
     * resulting encrypted password. If exception, the plain credentials
     * string is returned
     *
     * @param password Password or other credentials to use in authenticating
     *        this username
     * @param algorithm Algorithm used to do the digest
     *
     * @return encypted password based on the algorithm.
     */
    public static String encodePassword(String password, String algorithm) {
        byte[] unencodedPassword = password.getBytes();
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(algorithm);
        } catch (Exception e) {
            mLogger.error("Exception: " + e);
            return password;
        }
        md.reset();
        md.update(unencodedPassword);
        byte[] encodedPassword = md.digest();
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < encodedPassword.length; i++) {
            if ((encodedPassword[i] & 0xff) < 0x10) {
                buf.append("0");
            }
            buf.append(Long.toString(encodedPassword[i] & 0xff, 16));
        }
        return buf.toString();
    }

    /**
     * Encode a string using Base64 encoding. Used when storing passwords
     * as cookies.
     *
     * This is weak encoding in that anyone can use the decodeString
     * routine to reverse the encoding.
     *
     * @param str
     * @return String
     * @throws IOException
     */
    public static String encodeString(String str) throws IOException {
        sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
        String encodedStr = encoder.encodeBuffer(str.getBytes());
        return (encodedStr.trim());
    }

    /**
     * Decode a string using Base64 encoding.
     *
     * @param str
     * @return String
     * @throws IOException
     */
    public static String decodeString(String str) throws IOException {
        sun.misc.BASE64Decoder dec = new sun.misc.BASE64Decoder();
        String value = new String(dec.decodeBuffer(str));
        return (value);
    }

    /**
     * Strips HTML and truncates.
     */
    public static String truncate(String str, int lower, int upper, String appendToEnd) {
        String str2 = removeHTML(str, false);
        if (upper < lower) {
            upper = lower;
        }
        if (str2.length() > upper) {
            int loc;
            loc = str2.lastIndexOf(' ', upper);
            if (loc >= lower) {
                str2 = str2.substring(0, loc);
            } else {
                str2 = str2.substring(0, upper);
                loc = upper;
            }
            str2 = str2 + appendToEnd;
        }
        return str2;
    }

    /**
     * This method based on code from the String taglib at Apache Jakarta:
     * http://cvs.apache.org/viewcvs/jakarta-taglibs/string/src/org/apache/taglibs/string/util/StringW.java?rev=1.16&content-type=text/vnd.viewcvs-markup
     * Copyright (c) 1999 The Apache Software Foundation.
     * Author: timster@mac.com
     *
     * @param str
     * @param lower
     * @param upper
     * @param appendToEnd
     * @return
     */
    public static String truncateNicely(String str, int lower, int upper, String appendToEnd) {
        String str2 = removeHTML(str, false);
        boolean diff = (str2.length() < str.length());
        if (upper < lower) {
            upper = lower;
        }
        if (str2.length() > upper) {
            int loc;
            loc = str2.lastIndexOf(' ', upper);
            if (loc >= lower) {
                str2 = str2.substring(0, loc);
            } else {
                str2 = str2.substring(0, upper);
                loc = upper;
            }
            if (diff) {
                loc = str2.lastIndexOf(' ', loc);
                String str3 = str2.substring(loc + 1);
                loc = str.indexOf(str3, loc) + str3.length();
                str2 = str.substring(0, loc);
                str3 = extractHTML(str.substring(loc));
                str = str2 + appendToEnd + str3;
            } else {
                str = str2 + appendToEnd;
            }
        }
        return str;
    }

    public static String truncateText(String str, int lower, int upper, String appendToEnd) {
        String str2 = removeHTML(str, false);
        boolean diff = (str2.length() < str.length());
        if (upper < lower) {
            upper = lower;
        }
        if (str2.length() > upper) {
            int loc;
            loc = str2.lastIndexOf(' ', upper);
            if (loc >= lower) {
                str2 = str2.substring(0, loc);
            } else {
                str2 = str2.substring(0, upper);
                loc = upper;
            }
            str = str2 + appendToEnd;
        }
        return str;
    }

    /**
     * @param str
     * @return
     */
    private static String stripLineBreaks(String str) {
        str = str.replaceAll("<br>", "");
        str = str.replaceAll("<br/>", "");
        str = str.replaceAll("<br />", "");
        str = str.replaceAll("<p></p>", "");
        str = str.replaceAll("<p/>", "");
        str = str.replaceAll("<p />", "");
        return str;
    }

    /**
     * Need need to get rid of any user-visible HTML tags once all text has been
     * removed such as &lt;BR&gt;. This sounds like a better approach than removing
     * all HTML tags and taking the chance to leave some tags un-closed.
     *
     * WARNING: this method has serious performance problems a
     *
     * @author Alexis Moussine-Pouchkine <alexis.moussine-pouchkine@france.sun.com>
     * @author Lance Lavandowska
     * @param str the String object to modify
     * @return the new String object without the HTML "visible" tags
     */
    private static String removeVisibleHTMLTags(String str) {
        str = stripLineBreaks(str);
        StringBuffer result = new StringBuffer(str);
        StringBuffer lcresult = new StringBuffer(str.toLowerCase());
        String[] visibleTags = { "<img" };
        int stringIndex;
        for (int j = 0; j < visibleTags.length; j++) {
            while ((stringIndex = lcresult.indexOf(visibleTags[j])) != -1) {
                if (visibleTags[j].endsWith(">")) {
                    result.delete(stringIndex, stringIndex + visibleTags[j].length());
                    lcresult.delete(stringIndex, stringIndex + visibleTags[j].length());
                } else {
                    int endIndex = result.indexOf(">", stringIndex);
                    if (endIndex > -1) {
                        result.delete(stringIndex, endIndex + 1);
                        lcresult.delete(stringIndex, endIndex + 1);
                    }
                }
            }
        }
        String[] openCloseTags = { "li", "a", "div", "h1", "h2", "h3", "h4" };
        for (int j = 0; j < openCloseTags.length; j++) {
            String closeTag = "</" + openCloseTags[j] + ">";
            int lastStringIndex = 0;
            while ((stringIndex = lcresult.indexOf("<" + openCloseTags[j], lastStringIndex)) > -1) {
                lastStringIndex = stringIndex;
                int endIndex = lcresult.indexOf(closeTag, stringIndex);
                if (endIndex > -1) {
                    result.delete(stringIndex, endIndex + closeTag.length());
                    lcresult.delete(stringIndex, endIndex + closeTag.length());
                } else {
                    endIndex = lcresult.indexOf(">", stringIndex);
                    int nextStart = lcresult.indexOf("<", stringIndex + 1);
                    if (endIndex > stringIndex && lcresult.charAt(endIndex - 1) == '/' && (endIndex < nextStart || nextStart == -1)) {
                        result.delete(stringIndex, endIndex + 1);
                        lcresult.delete(stringIndex, endIndex + 1);
                    }
                }
            }
        }
        return result.toString();
    }

    /**
     * Extract (keep) JUST the HTML from the String.
     * @param str
     * @return
     */
    public static String extractHTML(String str) {
        if (str == null) return "";
        StringBuffer ret = new StringBuffer(str.length());
        int start = 0;
        int beginTag = str.indexOf("<");
        int endTag = 0;
        if (beginTag == -1) return str;
        while (beginTag >= start) {
            endTag = str.indexOf(">", beginTag);
            if (endTag > -1) {
                ret.append(str.substring(beginTag, endTag + 1));
                start = endTag + 1;
                beginTag = str.indexOf("<", start);
            } else {
                break;
            }
        }
        return ret.toString();
    }

    public static String hexEncode(String str) {
        if (StringUtils.isEmpty(str)) return str;
        return RegexUtil.encode(str);
    }

    public static String encodeEmail(String str) {
        return str != null ? RegexUtil.encodeEmail(str) : null;
    }

    /**
     * Converts a character to HTML or XML entity.
     *
     * @param ch The character to convert.
     * @param xml Convert the character to XML if set to true.
     * @author Erik C. Thauvin
     *
     * @return The converted string.
     */
    public static final String charToHTML(char ch, boolean xml) {
        int c;
        if (ch == '<') {
            return ("&lt;");
        } else if (ch == '>') {
            return ("&gt;");
        } else if (ch == '&') {
            return ("&amp;");
        } else if (xml && (ch == '"')) {
            return ("&quot;");
        } else if (xml && (ch == '\'')) {
            return ("&#39;");
        } else {
            return (String.valueOf(ch));
        }
    }

    /**
     * Converts a text string to HTML or XML entities.
     *
     * @author Erik C. Thauvin
     * @param text The string to convert.
     * @param xml Convert the string to XML if set to true.
     *
     * @return The converted string.
     */
    public static final String textToHTML(String text, boolean xml) {
        if (text == null) return "null";
        final StringBuffer html = new StringBuffer();
        for (int i = 0; i < text.length(); i++) {
            html.append(charToHTML(text.charAt(i), xml));
        }
        return html.toString();
    }

    /**
     * Converts a text string to HTML or XML entities.
     *
     * @param text The string to convert.
     * @author Erik C. Thauvin
     * @return The converted string.
     */
    public static final String textToHTML(String text) {
        return textToHTML(text, false);
    }

    /**
     * Converts a text string to XML entities.
     *
     * @param text The string to convert.
     * @author Erik C. Thauvin
     * @return The converted string.
     */
    public static final String textToXML(String text) {
        return textToHTML(text, true);
    }

    /**
     * Converts a text string to HTML or XML entities.
     * @param text The string to convert.
     * @return The converted string.
     */
    public static final String textToCDATA(String text) {
        if (text == null) return "null";
        final StringBuffer html = new StringBuffer();
        for (int i = 0; i < text.length(); i++) {
            html.append(charToCDATA(text.charAt(i)));
        }
        return html.toString();
    }

    /**
     * Converts a character to CDATA character.
     * @param ch The character to convert.
     * @return The converted string.
     */
    public static final String charToCDATA(char ch) {
        int c;
        if (ch >= 128) {
            c = ch;
            return ("&#" + c + ';');
        } else {
            return (String.valueOf(ch));
        }
    }

    /**
     * URL encoding.
     * @param s a string to be URL-encoded
     * @return URL encoding of s using character encoding UTF-8; null if s is null.
     */
    public static final String encode(String s) {
        try {
            if (s != null) return URLEncoder.encode(s, "UTF-8"); else return s;
        } catch (UnsupportedEncodingException e) {
            return s;
        }
    }

    /**
     * URL decoding.
     * @param s a URL-encoded string to be URL-decoded
     * @return URL decoded value of s using character encoding UTF-8; null if s is null.
     */
    public static final String decode(String s) {
        try {
            if (s != null) return URLDecoder.decode(s, "UTF-8"); else return s;
        } catch (UnsupportedEncodingException e) {
            return s;
        }
    }

    /**
     * @param string
     * @return
     */
    public static int stringToInt(String string) {
        try {
            return Integer.valueOf(string).intValue();
        } catch (NumberFormatException e) {
            mLogger.debug("Invalid Integer:" + string);
        }
        return 0;
    }

    /**
     * Code (stolen from Pebble) to add rel="nofollow" string to all links in HTML.
     */
    public static String addNofollow(String html) {
        if (html == null || html.length() == 0) {
            return html;
        }
        Matcher m = mLinkPattern.matcher(html);
        StringBuffer buf = new StringBuffer();
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            String link = html.substring(start, end);
            buf.append(html.substring(0, start));
            if (link.indexOf("rel=\"nofollow\"") == -1) {
                buf.append(link.substring(0, link.length() - 1) + " rel=\"nofollow\">");
            } else {
                buf.append(link);
            }
            html = html.substring(end, html.length());
            m = mLinkPattern.matcher(html);
        }
        buf.append(html);
        return buf.toString();
    }

    /**
     * Transforms the given String into a subset of HTML displayable on a web
     * page. The subset includes &lt;b&gt;, &lt;i&gt;, &lt;p&gt;, &lt;br&gt;,
     * &lt;pre&gt; and &lt;a href&gt; (and their corresponding end tags).
     *
     * @param s   the String to transform
     * @return    the transformed String
     */
    public static String transformToHTMLSubset(String s) {
        if (s == null) {
            return null;
        }
        s = replace(s, OPENING_B_TAG_PATTERN, "<b>");
        s = replace(s, CLOSING_B_TAG_PATTERN, "</b>");
        s = replace(s, OPENING_I_TAG_PATTERN, "<i>");
        s = replace(s, CLOSING_I_TAG_PATTERN, "</i>");
        s = replace(s, OPENING_BLOCKQUOTE_TAG_PATTERN, "<blockquote>");
        s = replace(s, CLOSING_BLOCKQUOTE_TAG_PATTERN, "</blockquote>");
        s = replace(s, BR_TAG_PATTERN, "<br />");
        s = replace(s, OPENING_P_TAG_PATTERN, "<p>");
        s = replace(s, CLOSING_P_TAG_PATTERN, "</p>");
        s = replace(s, OPENING_PRE_TAG_PATTERN, "<pre>");
        s = replace(s, CLOSING_PRE_TAG_PATTERN, "</pre>");
        s = replace(s, OPENING_UL_TAG_PATTERN, "<ul>");
        s = replace(s, CLOSING_UL_TAG_PATTERN, "</ul>");
        s = replace(s, OPENING_OL_TAG_PATTERN, "<ol>");
        s = replace(s, CLOSING_OL_TAG_PATTERN, "</ol>");
        s = replace(s, OPENING_LI_TAG_PATTERN, "<li>");
        s = replace(s, CLOSING_LI_TAG_PATTERN, "</li>");
        s = replace(s, QUOTE_PATTERN, "\"");
        s = replace(s, CLOSING_A_TAG_PATTERN, "</a>");
        Matcher m = OPENING_A_TAG_PATTERN.matcher(s);
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            String link = s.substring(start, end);
            link = "<" + link.substring(4, link.length() - 4) + ">";
            s = s.substring(0, start) + link + s.substring(end, s.length());
            m = OPENING_A_TAG_PATTERN.matcher(s);
        }
        s = s.replaceAll("&amp;lt;", "&lt;");
        s = s.replaceAll("&amp;gt;", "&gt;");
        s = s.replaceAll("&amp;#", "&#");
        return s;
    }

    private static String replace(String string, Pattern pattern, String replacement) {
        Matcher m = pattern.matcher(string);
        return m.replaceAll(replacement);
    }

    /**
     * Convert a byte array into a Base64 string (as used in mime formats)
     */
    public static String toBase64(byte[] aValue) {
        final String m_strBase64Chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        int byte1;
        int byte2;
        int byte3;
        int iByteLen = aValue.length;
        StringBuffer tt = new StringBuffer();
        for (int i = 0; i < iByteLen; i += 3) {
            boolean bByte2 = (i + 1) < iByteLen;
            boolean bByte3 = (i + 2) < iByteLen;
            byte1 = aValue[i] & 0xFF;
            byte2 = (bByte2) ? (aValue[i + 1] & 0xFF) : 0;
            byte3 = (bByte3) ? (aValue[i + 2] & 0xFF) : 0;
            tt.append(m_strBase64Chars.charAt(byte1 / 4));
            tt.append(m_strBase64Chars.charAt((byte2 / 16) + ((byte1 & 0x3) * 16)));
            tt.append(((bByte2) ? m_strBase64Chars.charAt((byte3 / 64) + ((byte2 & 0xF) * 4)) : '='));
            tt.append(((bByte3) ? m_strBase64Chars.charAt(byte3 & 0x3F) : '='));
        }
        return tt.toString();
    }
}
