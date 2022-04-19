package com.codeplanet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class Test {
	@GetMapping("/urlshortner")
	public String UrlShortner(HttpServletRequest req) throws ClassNotFoundException, SQLException {
		String link = req.getParameter("link");
		String customurl = req.getParameter("customurl");
		if(customurl != null && !customurl.isEmpty()) {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test","root","root");
			Statement stmt = con.createStatement();
			String query1 = "Select * from links where short_link='"+customurl+"'";
			ResultSet rs = stmt.executeQuery(query1);
			if(rs.next()) {
				req.setAttribute("error","custom Url Already exist");
			}
			else {
				String query2 = "insert into links(long_link,short_link) values(?,?)";
				PreparedStatement stmt1 = con.prepareStatement(query2);
				stmt1.setString(1, link);
				stmt1.setString(2, customurl);
				stmt1.executeUpdate();
				req.setAttribute("url", "Your new url is nano.cc/"+customurl);
			}
		}
		else {
			
		}
		return "home";
	}
	@GetMapping("/{url}")
	public String handleShortUrl(@PathVariable String url) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test","root","root");
		PreparedStatement pstmt = con.prepareStatement("Select * from links where short_link = ?");
		pstmt.setString(1, url);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()) {
			String longlink = rs.getString("long_link");
			return "redirect:"+longlink;
		}
		else {
			return "error";
		}
	}
}
