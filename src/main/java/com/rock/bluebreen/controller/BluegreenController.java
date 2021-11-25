package com.rock.bluebreen.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BluegreenController {

	@Value("${app.version}")
	private String version;

	@Value("${app.name}")
	private String name;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@GetMapping("/hello")
	HashMap<Object, Object> hello() throws UnknownHostException {

		long start = System.currentTimeMillis();
		HashMap<Object, Object> source = new HashMap<Object, Object>();

		source.put("pageCode", "0002");
		source.put("ip", InetAddress.getLocalHost().getHostAddress().toString());
		source.put("hostname", InetAddress.getLocalHost().getHostName().toString());
		source.put("app", name);
		source.put("version", version);

		long finish = System.currentTimeMillis();
		long timeElapsed = finish - start;

		source.put("timeElapsed", timeElapsed);

		return source;
	}

	@GetMapping("/ping")
	HashMap<Object, Object> ping() {

		long start = System.currentTimeMillis();
		HashMap<Object, Object> source = new HashMap<Object, Object>();

		source.put("pageCode", "0001");
		source.put("ping", "pong");
		source.put("app", name);
		source.put("version", version);

		try {
			jdbcTemplate.execute("select 1");
			source.put("database", "ok");
		} catch (Exception e) {
			source.put("database", "error");
		}

		long finish = System.currentTimeMillis();
		long timeElapsed = finish - start;

		source.put("timeElapsed", timeElapsed);

		return source;
	}

	@GetMapping("/sleep")
	HashMap<Object, Object> sleep(@RequestParam(defaultValue = "1") Long sleep) throws InterruptedException {

		long start = System.currentTimeMillis();
		HashMap<Object, Object> source = new HashMap<Object, Object>();

		Thread.sleep(sleep * 1000);

		source.put("pageCode", "0003");
		source.put("app", name);
		source.put("version", version);

		long finish = System.currentTimeMillis();
		long timeElapsed = finish - start;

		source.put("timeExpected", sleep * 1000);
		source.put("timeElapsed", timeElapsed);

		return source;
	}

	@GetMapping("/db/count")
	HashMap<Object, Object> select(@RequestParam(defaultValue = "information_schema.tables") String table) {

		long start = System.currentTimeMillis();
		HashMap<Object, Object> source = new HashMap<Object, Object>();

		source.put("pageCode", "0004");
		source.put("ping", "pong");
		source.put("app", name);
		source.put("version", version);

		try {
			int count = jdbcTemplate.queryForObject("select count(*) from " + table, Integer.class);
			source.put("table", table);
			source.put("count", count);
		} catch (Exception e) {
			e.printStackTrace();
			source.put("database", "error");
		}

		long finish = System.currentTimeMillis();
		long timeElapsed = finish - start;

		source.put("timeElapsed", timeElapsed);

		return source;
	}
}
