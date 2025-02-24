package cn.neday.sheep.viewmodel

import androidx.lifecycle.MutableLiveData
import cn.neday.sheep.model.RankingGoods
import cn.neday.sheep.network.repository.GoodsRepository
import cn.neday.sheep.network.requestAsync
import cn.neday.sheep.network.then

/**
 * RankingListViewModel
 *
 * @author nEdAy
 */
class RankingListViewModel(private val repository: GoodsRepository) : BaseViewModel() {

    val rankGoods: MutableLiveData<List<RankingGoods>> = MutableLiveData()

    fun getRankingList(rankType: Int, cid: String = "") {
        requestAsync {
            repository.getRankingList(rankType, cid)
        }.then({
            rankGoods.value = it.data
        }, {
            errMsg.value = it
        }, {
            onComplete.call()
        })
    }
}