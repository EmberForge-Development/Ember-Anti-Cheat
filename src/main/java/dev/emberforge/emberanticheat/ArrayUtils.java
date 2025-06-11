package dev.emberforge.emberanticheat;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.util.ArrayList;
import java.util.List;

public class ArrayUtils {
	public static boolean nameContains(ArrayList<String> list, String string) {
		for(int i =0; i < list.size(); i++) {
			if(string.contains(list.get(i))) {
				return true;
			}
		}
		return false;
	}
	
	public static String[] SplitStringByByteLength(String src,String encoding, int maxsize) {
	    Charset cs = Charset.forName(encoding);
	    CharsetEncoder coder = cs.newEncoder();
	    ByteBuffer out = ByteBuffer.allocate(maxsize);  // output buffer of required size
	    CharBuffer in = CharBuffer.wrap(src);
	    List<String> ss = new ArrayList<>();            // a list to store the chunks
	    int pos = 0;
	    while(true) {
	        CoderResult cr = coder.encode(in, out, true); // try to encode as much as possible
	        int newpos = src.length() - in.length();
	        String s = src.substring(pos, newpos);
	        ss.add(s);                                  // add what has been encoded to the list
	        pos = newpos;                               // store new input position
	        out.rewind();                               // and rewind output buffer
	        if (! cr.isOverflow()) {
	            break;                                  // everything has been encoded
	        }
	    }
	    return ss.toArray(new String[0]);
	}
}
