package net.bzk.flow.run.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class LocalModelService {

	public static final String ROOT_DIR = "/bzk/model/";

	public static void main(String args[]) {
		List<String> ans = new ArrayList<>();
		rec( new char[2], 0, ans);
		ans.forEach(System.out::println);
	}

	public static void rec( char[] chs, int chidx, List<String> l) {
		if (chs.length == chidx) {
			l.add(new String(chs));
			return;
		}
		chs[chidx] = 'x';
		rec( chs, chidx + 1, l);
		chs[chidx] = 'y';
		 rec( chs, chidx + 1, l);
	}

}
