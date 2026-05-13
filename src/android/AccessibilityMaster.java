/*
 * AccessibilityMaster.java
 *
 * Cordova Plugin:
 * cordova-plugin-accessibility-check
 *
 * Created by Tamour Ahmad
 *
 * Description:
 * Android Cordova plugin for checking:
 * - Accessibility master setting
 * - Accessibility active state
 * - Enabled accessibility services
 *
 * License: MIT
 */
 
package com.plugin.accessibility;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import android.provider.Settings;
import android.content.Context;
import android.view.accessibility.AccessibilityManager;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

/**
 * AccessibilityMaster
 *
 * Cordova plugin used to interact with Android Accessibility settings.
 *
 * Features:
 * 1. Check whether Android Accessibility is globally enabled.
 * 2. Check whether Accessibility services are currently active via AccessibilityManager.
 * 3. Retrieve the list of enabled accessibility services.
 *
 * This plugin exposes methods callable from JavaScript through Cordova.
 */
public class AccessibilityMaster extends CordovaPlugin {

    /**
     * Executes plugin actions sent from JavaScript.
     *
     * Supported actions:
     * - isMasterEnabled    -> Checks global accessibility toggle state.
     * - isServiceActive    -> Checks whether accessibility services are active.
     * - getEnabledServices -> Returns enabled accessibility services list.
     *
     * @param action           Action name received from JavaScript.
     * @param args             Arguments passed from JavaScript (unused here).
     * @param callbackContext  Callback used to send results back to JavaScript.
     * @return true if action handled, otherwise false.
     * @throws JSONException if JSON parsing fails.
     */
    @Override
    public boolean execute(
            String action,
            JSONArray args,
            CallbackContext callbackContext
    ) throws JSONException {

        // Check if Android accessibility master switch is enabled
        if (action.equals("isMasterEnabled")) {
            this.checkMasterSetting(callbackContext);
            return true;

        // Check if accessibility service is currently active
        } else if (action.equals("isServiceActive")) {
            this.checkActiveService(callbackContext);
            return true;

        // Retrieve all enabled accessibility services
        } else if (action.equals("getEnabledServices")) {
            this.getEnabledServices(callbackContext);
            return true;
        }

        // Action not recognized
        return false;
    }

    /**
     * Method 1:
     * Checks Android's global Accessibility setting
     * from Settings.Secure.ACCESSIBILITY_ENABLED.
     *
     * Returns:
     * - 1 if accessibility is enabled
     * - 0 if accessibility is disabled
     *
     * This reflects the master accessibility toggle in system settings.
     *
     * @param callbackContext Callback used to return result/errors.
     */
    private void checkMasterSetting(CallbackContext callbackContext) {
        try {
            // Get application context
            Context context = this.cordova
                    .getActivity()
                    .getApplicationContext();

            // Read global accessibility enabled flag
            int isEnabled = Settings.Secure.getInt(
                    context.getContentResolver(),
                    Settings.Secure.ACCESSIBILITY_ENABLED,
                    0
            );

            // Return success result to JavaScript
            callbackContext.success(isEnabled == 1 ? 1 : 0);

        } catch (Exception e) {

            // Return error message to JavaScript
            callbackContext.error(
                    "Error reading settings: " + e.getMessage()
            );
        }
    }

    /**
     * Method 2:
     * Uses AccessibilityManager to determine whether
     * accessibility services are currently active.
     *
     * Returns:
     * - 1 if active
     * - 0 if inactive
     *
     * Unlike the secure setting check, this reflects
     * the runtime accessibility manager state.
     *
     * @param callbackContext Callback used to return result/errors.
     */
    private void checkActiveService(CallbackContext callbackContext) {
        try {
            // Get application context
            Context context = this.cordova
                    .getActivity()
                    .getApplicationContext();

            // Obtain AccessibilityManager instance
            AccessibilityManager am =
                    (AccessibilityManager) context.getSystemService(
                            Context.ACCESSIBILITY_SERVICE
                    );

            // Check whether accessibility is enabled
            boolean isEnabled = am != null && am.isEnabled();

            // Return result to JavaScript
            callbackContext.success(isEnabled ? 1 : 0);

        } catch (Exception e) {

            // Return error message
            callbackContext.error(
                    "Error checking manager: " + e.getMessage()
            );
        }
    }

    /**
     * Method 3:
     * Retrieves all enabled accessibility services.
     *
     * Android stores enabled services as a colon-separated string
     * inside Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES.
     *
     * Example:
     * com.example.service/.MyAccessibilityService
     *
     * Response format:
     * [
     *   { "id": "service1" },
     *   { "id": "service2" }
     * ]
     *
     * @param callbackContext Callback used to return JSON results/errors.
     */
    private void getEnabledServices(CallbackContext callbackContext) {

        try {
            // Get application context
            Context context = this.cordova
                    .getActivity()
                    .getApplicationContext();

            // Read enabled accessibility services string
            String enabledServices = Settings.Secure.getString(
                    context.getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            );

            // JSON array to hold service list
            JSONArray results = new JSONArray();

            // Ensure value exists before processing
            if (enabledServices != null && !enabledServices.isEmpty()) {

                // Services are separated using ":"
                String[] services = enabledServices.split(":");

                // Convert each service into JSON object
                for (String service : services) {
                    results.put(service);
                }
            }

            // Return service list to JavaScript
            callbackContext.success(results);

        } catch (Exception e) {

            // Return error message
            callbackContext.error(
                    "Error listing services: " + e.getMessage()
            );
        }
    }
}