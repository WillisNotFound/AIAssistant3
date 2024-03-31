package com.willis.ai_assistant3.ui.mine

import androidx.lifecycle.ViewModel
import com.willis.ai_assistant3.repo.appRepo
import kotlinx.coroutines.flow.StateFlow

/**
 * description: none
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/15
 */
class MineViewModel : ViewModel() {
    val mPhoneFlow: StateFlow<String?> = appRepo.currentPhoneFlow
}