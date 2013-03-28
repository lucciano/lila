package lila.i18n

import lila.db.Repo
import lila.db.Implicits._

import play.api.libs.json.Json
import play.api.libs.concurrent.Execution.Implicits._

import reactivemongo.api._
import reactivemongo.bson._
import reactivemongo.core.commands._

private[i18n] final class TranslationRepo(implicit coll: Coll)
    extends Repo[Int, Translation](Translations.json) {

  type ID = Int

  val nextId: Fu[ID] = primitive.one(
    select.all,
    "_id",
    _ sort sort.descId
  )(_.asOpt[Int]) map (opt ⇒ ~opt + 1)

  def findFrom(id: ID): Fu[List[Translation]] =
    find(query(Json.obj("_id" -> $lte(id))) sort sort.ascId)
}