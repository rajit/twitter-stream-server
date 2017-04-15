import jawn.Facade
import jawnstreamz._
import org.http4s._
import org.http4s.client.blaze._
import org.http4s.client.oauth1

import scalaz.concurrent.Task
import scalaz.stream.Process

class StreamTwit(consumerKey: String, consumerSecret: String, accessToken: String, accessSecret: String) {

  // Remember, this `Client` needs to be cleanly shutdown
  val client = PooledHttp1Client()

  /* These values are created by a Twitter developer web app.
   * OAuth signing is an effect due to generating a nonce for each `Request`.
   */
  def sign(req: Request): Task[Request] = {
    val consumer = oauth1.Consumer(consumerKey, consumerSecret)
    val token    = oauth1.Token(accessToken, accessSecret)
    oauth1.signRequest(req, consumer, callback = None, verifier = None, token = Some(token))
  }

  /* Sign the incoming `Request`, stream the `Response`, and `parseJsonStream` the `Response`.
   * `sign` returns a `Task`, so we need to `Process.eval` it to use a for-comprehension.
   */
  def stream[J](req: Request)(implicit facade: Facade[J]): Process[Task, J] =
    for {
      sr  <- Process.eval(sign(req))
      res <- client.streaming(sr)(resp => resp.body.parseJsonStream)
    } yield res

  def sample[J](implicit facade: Facade[J]): Process[Task, J] =
    stream(Request(Method.GET, Uri.uri("https://stream.twitter.com/1.1/statuses/sample.json")))

  def shutdown: Task[Unit] = client.shutdown

}
