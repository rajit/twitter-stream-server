import io.circe.Printer
import org.http4s._
import org.http4s.circe.CirceInstances
import org.http4s.dsl._
import org.http4s.server.blaze._
import org.http4s.server.{Server, ServerApp}

import scalaz.concurrent.Task

object Main extends ServerApp {
  // jawnstreamz needs to know what JSON AST you want
  implicit val f = io.circe.jawn.CirceSupportParser.facade
  implicit val j = CirceInstances.withPrinter(Printer.noSpaces).jsonEncoder

  val client = new StreamTwit(
    sys.env("CONSUMER_KEY"), sys.env("CONSUMER_SECRET"), sys.env("ACCESS_KEY"), sys.env("ACCESS_SECRET"))

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
