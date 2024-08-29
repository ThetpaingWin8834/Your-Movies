package com.ym.yourmovies.utils.others

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension

object Helper {
    @Composable
    fun cmMovieLinearConstraint (): ConstraintSet = remember {
        ConstraintSet{
            val title = createRefFor("title")
            val thumb  = createRefFor("thumb")
            val desc  = createRefFor("desc")
            val rating  = createRefFor("rating")
            val guide35 = createGuidelineFromStart(0.35f)
            constrain(title){
                width = Dimension.fillToConstraints
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                end.linkTo(rating.start)

            }
            constrain(thumb){
                width = Dimension.fillToConstraints
                height = Dimension.ratio("1:1.5")
                start.linkTo(parent.start)
                end.linkTo(guide35)
                top.linkTo(title.bottom)
            }
            constrain(desc){
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
                start.linkTo(guide35)
                end.linkTo(parent.end)
                top.linkTo(thumb.top)
                bottom.linkTo(parent.bottom)
            }
            constrain(rating){
                width = Dimension.fillToConstraints
                end.linkTo(parent.end)
                top.linkTo(parent.top)
            }
        }
    }
    @Composable
     fun cmMovieGridConstraint (): ConstraintSet = remember {
        ConstraintSet{
            val title = createRefFor("title")
            val thumb  = createRefFor("thumb")
            val rating  = createRefFor("rating")
            constrain(thumb){
                width = Dimension.matchParent
                height = Dimension.ratio("1:1.35")
//                start.linkTo(parent.start)
//                end.linkTo(parent.end)
//                top.linkTo(parent.top)
            }
            constrain(rating){
                end.linkTo(thumb.end)
                bottom.linkTo(thumb.bottom)
            }
            constrain(title){
                start.linkTo(parent.start)
                top.linkTo(thumb.bottom)
                end.linkTo(parent.end)

            }


        }
     }

    @Composable
    fun gcMovieLinearConstraintSet() : ConstraintSet = remember {
        ConstraintSet{
            val thumb = createRefFor("thumb")
            val title = createRefFor("title")
            val rating = createRefFor("rating")
            val date = createRefFor("date")
            val guide35 = createGuidelineFromStart(0.35f)
            constrain(thumb){
                width = Dimension.fillToConstraints
                height = Dimension.ratio("1:1.5")
                start.linkTo(parent.start)
                end.linkTo(guide35)
                top.linkTo(parent.top)
            }
            constrain(title){
                start.linkTo(guide35)
                end.linkTo(parent.end)
                top.linkTo(parent.top)
                bottom.linkTo(date.top)
            }
            constrain(date){
                start.linkTo(guide35)
                end.linkTo(rating.start)
                top.linkTo(title.bottom)
                bottom.linkTo(parent.bottom)
            }
            constrain(rating){
                start.linkTo(date.end)
                top.linkTo(title.bottom)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }
//            createHorizontalChain(date,rating)
//            createVerticalChain(title,date,rating)
        }
    }

    @Composable
    fun mSubMovieGridConstraintSet() :ConstraintSet = remember {
        ConstraintSet{
            val title = createRefFor("title")
            val thumb  = createRefFor("thumb")
            val rating  = createRefFor("rating")
            val date  = createRefFor("date")
            constrain(thumb){
                width = Dimension.matchParent
                height = Dimension.ratio("1:1.35")
            }
            constrain(rating){
                end.linkTo(thumb.end)
                bottom.linkTo(thumb.bottom)
            }
            constrain(title){
                start.linkTo(parent.start)
                top.linkTo(thumb.bottom)
                end.linkTo(parent.end)

            }
            constrain(date){
                start.linkTo(parent.start)
                top.linkTo(title.bottom)
                end.linkTo(parent.end)
            }
        }
    }
    @Composable
    fun mSubMovieLinearConstraintSet (): ConstraintSet = remember {
        ConstraintSet{
            val title = createRefFor("title")
            val thumb  = createRefFor("thumb")
            val date  = createRefFor("date")
            val rating  = createRefFor("rating")
            val guide35 = createGuidelineFromStart(0.35f)
            constrain(title){
                width = Dimension.fillToConstraints
                start.linkTo(guide35)
                top.linkTo(parent.top)
                end.linkTo(parent.end)
                bottom.linkTo(rating.top)

            }
            constrain(thumb){
                width = Dimension.fillToConstraints
                height = Dimension.ratio("1:1.5")
                start.linkTo(parent.start)
                end.linkTo(guide35)
                top.linkTo(parent.top)
            }
            constrain(date){
               start.linkTo(rating.end)
                end.linkTo(parent.end)
                top.linkTo(title.bottom)
                bottom.linkTo(thumb.bottom)
            }
            constrain(rating){
                start.linkTo(guide35)
                end.linkTo(date.start)
                top.linkTo(title.bottom)
                bottom.linkTo(thumb.bottom)
            }
        }
    }

    @Composable
    fun gcMovieGridConstraintSet() : ConstraintSet = remember {
        ConstraintSet{
            val title = createRefFor("title")
            val date = createRefFor("date")
            val rating = createRefFor("rating")
            val thumb = createRefFor("thumb")
            constrain(thumb){
                width = Dimension.matchParent
                height = Dimension.ratio("1:1.35")
            }
            constrain(rating){
                end.linkTo(thumb.end)
                bottom.linkTo(thumb.bottom)
            }
            constrain(title){
                top.linkTo(thumb.bottom)
                start.linkTo(parent.start)
            }
            constrain(date){
                top.linkTo(title.bottom)
                start.linkTo(parent.start)
            }
        }
    }
}