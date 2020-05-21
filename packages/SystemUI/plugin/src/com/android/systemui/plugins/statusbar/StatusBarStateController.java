/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.systemui.plugins.statusbar;

import com.android.systemui.plugins.annotations.DependsOn;
import com.android.systemui.plugins.annotations.ProvidesInterface;


/**
 * Sends updates to {@link StateListener}s about changes to the status bar state and dozing state
 */
@ProvidesInterface(version = StatusBarStateController.VERSION)
@DependsOn(target = StatusBarStateController.StateListener.class)
public interface StatusBarStateController {
    int VERSION = 1;

    /**
     * Current status bar state
     */
    int getState();

    /**
     * Is device dozing
     */
    boolean isDozing();

    /**
     * Adds a state listener
     */
    void addCallback(StateListener listener);

    /**
     * Removes callback from listeners
     */
    void removeCallback(StateListener listener);

    /**
     * Get amount of doze
     */
    float getDozeAmount();

    /**
     * Listener for StatusBarState updates
     */
    @ProvidesInterface(version = StateListener.VERSION)
    public interface StateListener {
        int VERSION = 1;

        /**
         * Callback before the new state is applied, for those who need to preempt the change.
         */
        default void onStatePreChange(int oldState, int newState) {
        }

        /**
         * Callback after all listeners have had a chance to update based on the state change
         */
        default void onStatePostChange() {
        }

        /**
         * Required callback. Get the new state and do what you will with it. Keep in mind that
         * other listeners are typically unordered and don't rely on your work being done before
         * other peers.
         *
         * Only called if the state is actually different.
         */
        default void onStateChanged(int newState) {
        }

        /**
         * Callback to be notified when Dozing changes. Dozing is stored separately from state.
         */
        default void onDozingChanged(boolean isDozing) {}

        /**
         * Callback to be notified when the doze amount changes. Useful for animations.
         * Note: this will be called for each animation frame. Please be careful to avoid
         * performance regressions.
         */
        default void onDozeAmountChanged(float linear, float eased) {}

        /**
         * Callback to be notified when the sysui visibility changes
         */
        default void onSystemUiVisibilityChanged(int visibility) {}

        /**
         * Callback to be notified when the pulsing state changes
         */
        default void onPulsingChanged(boolean pulsing) {}
    }
}