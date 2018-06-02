import scalikejdbc._
import scalikejdbc.config._

case class Sample(id: Int, name: Option[String])

object Sample extends SQLSyntaxSupport[Sample] {
  override val tableName = "simple_table"

  def apply(rs: WrappedResultSet): Sample =
    new Sample(rs.int("id"), rs.stringOpt("name"))

  def apply(u: ResultName[Sample])(rs: WrappedResultSet): Sample =
    Sample(rs.int(u.id), rs.stringOpt(u.name))
}

object Main extends App {
  // `/main/resources/application.conf`内で設定した通りにdbをセットアップ
  DBs.setupAll()
  implicit val session: AutoSession = AutoSession

  // query all
  val sample = Sample.syntax("sample")
  val entities: List[Sample] =
    withSQL { select.from(Sample as sample) }
      .map(Sample(sample.resultName))
      .list
      .apply()

  // view all table value
  for (member <- entities;
       name <- member.name) {
    println(s"id = ${member.id}, name = $name")
  }

  DBs.closeAll()
}
