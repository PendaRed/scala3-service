package com.jgibbons.svcfirst

import java.util.Properties
import com.zaxxer.hikari.HikariConfig
import com.typesafe.config.{Config, ConfigFactory}

case class ServerCfg(hostName: String, portNum: Int)

case class MicroServiceConfig(cfg: Config) {
  val dbConfig: HikariConfig = createDbConfig(cfg.getConfig("db.hikariProperties"))
  val serverConfig: ServerCfg = createServerConfig(cfg.getConfig("server"))

  private def createDbConfig(c: Config) =
    MicroServiceConfig.toProperties(c)

  private def createServerConfig(c: Config): ServerCfg =
    ServerCfg(c.getString("host"), c.getInt("port"))

}

object MicroServiceConfig {
  def apply(): (ServerCfg, HikariConfig) = {
    val cfg = ConfigFactory.load()
    val c = MicroServiceConfig(cfg)
    (c.serverConfig, c.dbConfig)
  }

  def toProperties(config: Config): HikariConfig = {
    val properties = new Properties()
    config.entrySet.forEach(e => properties.setProperty(e.getKey, config.getString(e.getKey)))
    new HikariConfig(properties)
  }
}
