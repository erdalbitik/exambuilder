package com.ebitik.exambuilder.service;

import java.io.File;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

public class QuestionSizeMap {

	public static void putSize(String xhtml, Integer size) {
		String digest = DigestUtils.sha1Hex(xhtml);
		try (DB questionSizeDb = DBMaker
				.fileDB(System.getProperty("user.home")+File.separator+"exambuilder.db")
				.fileMmapEnable()
				.make();) {
			Map<String, Integer> questionSizeMap = questionSizeDb
					.hashMap("questionSizeMap", Serializer.STRING, Serializer.INTEGER)
					.createOrOpen();
			questionSizeMap.put(digest, size);
			questionSizeDb.commit();
		}
	}

	public static Integer getSize(String xhtml) {
		String digest = DigestUtils.sha1Hex(xhtml);
		try (DB questionSizeDb = DBMaker
				.fileDB(System.getProperty("user.home")+File.separator+"exambuilder.db")
				.fileMmapEnable()
				.make();) {
			Map<String, Integer> questionSizeMap = questionSizeDb
					.hashMap("questionSizeMap", Serializer.STRING, Serializer.INTEGER)
					.createOrOpen();
			return questionSizeMap.get(digest);
		}
	}

}
