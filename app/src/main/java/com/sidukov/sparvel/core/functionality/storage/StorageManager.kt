package com.sidukov.sparvel.core.functionality.storage

import com.sidukov.sparvel.core.functionality.storage.SharedPrefsManager
import javax.inject.Inject

// add room dao here later
class StorageManager @Inject constructor(
    val settings: SharedPrefsManager
) {

}