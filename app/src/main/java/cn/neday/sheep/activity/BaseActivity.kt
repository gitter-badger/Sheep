package cn.neday.sheep.activity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.ali.auth.third.login.LoginConstants.TOKEN
import com.alibaba.baichuan.android.trade.AlibcTradeSDK
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.StringUtils
import com.tencent.mmkv.MMKV
import com.umeng.analytics.MobclickAgent

/**
 * Activity基类
 *
 * @author nEdAy
 */
abstract class BaseActivity(@get:LayoutRes val layoutId: Int?) : AppCompatActivity() {

    val kv: MMKV = MMKV.defaultMMKV()

    open val isCheckLogin = false

    lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isCheckLogin && StringUtils.isTrimEmpty(kv.decodeString(TOKEN))) {
            ActivityUtils.startActivity(LoginActivity::class.java)
            ActivityUtils.finishActivity(this)
        }
        mContext = this
        layoutId?.let {
            setContentView(LayoutInflater.from(this).inflate(it, null))
        }
        prepareInitView()
        initView()
    }

    open fun prepareInitView() {
        // do nothing
    }

    /**
     * onCreate
     */
    abstract fun initView()

    override fun onResume() {
        super.onResume()
        MobclickAgent.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        MobclickAgent.onPause(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        AlibcTradeSDK.destory()
    }
}