/**
 * AccessibilityMaster Cordova JavaScript Interface
 *
 * This file acts as the JavaScript bridge between the Cordova app
 * and the native Android AccessibilityMaster plugin implementation.
 *
 * The Cordova `exec` function is used to invoke native Java methods.
 */

// Import Cordova exec bridge
var exec = require('cordova/exec');

/**
 * Checks whether Android Accessibility is globally enabled.
 *
 * Native method called:
 * AccessibilityMaster.checkMasterSetting()
 *
 * Result:
 * - success callback receives:
 *      1 -> Accessibility enabled
 *      0 -> Accessibility disabled
 *
 * @param {Function} success Callback executed on success.
 * @param {Function} error   Callback executed on failure.
 */
exports.isMasterEnabled = function (success, error) {

    exec(
        success,                 // Success callback
        error,                   // Error callback
        'AccessibilityMaster',  // Native plugin class name
        'isMasterEnabled',      // Action name
        []                      // Arguments array (none required)
    );
};

/**
 * Checks whether Accessibility services are currently active.
 *
 * Native method called:
 * AccessibilityMaster.checkActiveService()
 *
 * Result:
 * - success callback receives:
 *      1 -> Service active
 *      0 -> Service inactive
 *
 * @param {Function} success Callback executed on success.
 * @param {Function} error   Callback executed on failure.
 */
exports.isServiceActive = function (success, error) {

    exec(
        success,
        error,
        'AccessibilityMaster',
        'isServiceActive',
        []
    );
};

/**
 * Retrieves a list of enabled accessibility services.
 *
 * Native method called:
 * AccessibilityMaster.getEnabledServices()
 *
 * Result Example:
 * [
 *   { id: "com.example.service/.MyService" },
 *   { id: "com.android.talkback/.TalkBackService" }
 * ]
 *
 * @param {Function} success Callback executed with service list.
 * @param {Function} error   Callback executed on failure.
 */
exports.getEnabledServices = function (success, error) {

    exec(
        success,
        error,
        'AccessibilityMaster',
        'getEnabledServices',
        []
    );
};