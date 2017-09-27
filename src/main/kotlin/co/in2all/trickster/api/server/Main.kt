package co.in2all.trickster.api.server

import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet

fun main(args: Array<String>) {
    Application.start()
//    val con: Connection = DriverManager.getConnection("jdbc:neo4j:bolt://localhost", "neo4j", "samplepassword")
//    val query = "MATCH (n) return n"
//    val stmt: PreparedStatement = con.prepareStatement(query)
//
//    val rs: ResultSet = stmt.executeQuery()
//    while (rs.next()) {
//        println(rs.getString("n"))
//    }

}
