import io.circe.Printer
import org.http4s._
import org.http4s.circe.CirceInstances
import org.http4s.dsl._
import org.http4s.server.blaze._
import org.http4s.server.{Server, ServerApp}

import scalaz.concurrent.Task

object Main extends ServerApp with ServerConfig {
  val client = new StreamTwit(consumerKey, consumerSecret, accessKey, accessSecret)

  val service = HttpService {
    case GET -> Root / "sample-stream" =>
      Ok(client.sample)

    case GET -> Root / sample =>
      Ok(client.sample.take(5))
  }

  override def server(args: List[String]): Task[Server] = {
    BlazeBuilder
      .bindHttp(8080, "localhost")
      .mountService(service, "/")
      .start
  }
}

trait ServerConfig {
  implicit val jsonFacade = io.circe.jawn.CirceSupportParser.facade
  implicit val jsonEncoder = CirceInstances.withPrinter(Printer.noSpaces).jsonEncoder

  val consumerKey = sys.env("CONSUMER_KEY")
  val consumerSecret = sys.env("CONSUMER_SECRET")
  val accessKey = sys.env("ACCESS_KEY")
  val accessSecret = sys.env("ACCESS_SECRET")
}
