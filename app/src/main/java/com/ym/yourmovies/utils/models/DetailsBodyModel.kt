package com.ym.yourmovies.utils.models

class DetailsBodyData(
    val title: String,
   val detailsBodyModel: DetailsBodyModel
) {
    companion object{
        fun emptyDetailsBodyData () = DetailsBodyData(
            title = "",
            detailsBodyModel = DetailsBodyModel.Nothing
        )
    }
}
sealed interface DetailsBodyModel{
    class Synopsis (val htmlString:String):DetailsBodyModel
    class Trailers(val trailersList: List<String>) : DetailsBodyModel
    class AllCasts(val allCastsList: List<Casts>) :DetailsBodyModel
    object Nothing : DetailsBodyModel
}
class Casts(
    val title:String,
    val castList: List<CastMetaData>
)
data class CastMetaData(
    val realname :String ,
    val movieName :String?=null ,
    val thumb :String?=null ,
    val url :String?=null
)