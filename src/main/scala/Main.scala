import scalikejdbc._
import scalikejdbc.config._

case class Sample(id: Int, name: Option[String])
object Sample extends SQLSyntaxSupport[Sample] {
  override val tableName = "simple_table"

  def apply(rs: WrappedResultSet): Sample =
    new Sample(rs.int("id"), rs.stringOpt("name"))
}

object Main extends App {
  // `/main/resources/application.conf`内で設定した通りにdbをセットアップ
  DBs.setupAll()
  implicit val session: AutoSession = AutoSession

  // query all
  val entities: List[Map[String, Any]] =
    sql"select * from simple_table".map(_.toMap).list.apply()

  // view all table value
  for (member <- entities) {
    for (id <- member.get("id");
         name <- member.get("name")) {
      println(s"id = $id, name = $name")
    }
  }
}
