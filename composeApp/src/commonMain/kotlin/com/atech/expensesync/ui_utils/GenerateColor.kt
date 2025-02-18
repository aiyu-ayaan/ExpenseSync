package com.atech.expensesync.ui_utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import kotlin.random.Random

/**
 * Generates a list of random colors.
 *
 * @param count Number of colors to generate
 * @param hueRange Range of hue values (0-360)
 * @param saturationRange Range of saturation values (0-1)
 * @param brightnessRange Range of brightness values (0-1)
 * @return List of randomly generated colors
 */
@Composable
fun generateRandomColors(
    count: Int,
    hueRange: ClosedFloatingPointRange<Float> = 0f..360f,
    saturationRange: ClosedFloatingPointRange<Float> = 0.5f..0.9f,
    brightnessRange: ClosedFloatingPointRange<Float> = 0.7f..0.9f
): List<Color> {
    return remember {
        List(count) {
            val hue = Random.nextFloat() * (hueRange.endInclusive - hueRange.start) + hueRange.start
            val saturation =
                Random.nextFloat() * (saturationRange.endInclusive - saturationRange.start) + saturationRange.start
            val brightness =
                Random.nextFloat() * (brightnessRange.endInclusive - brightnessRange.start) + brightnessRange.start

            Color.hsl(hue, saturation, brightness)
        }
    }
}

/**
 * Generates a list of colors that are visually pleasant together.
 * Uses a base hue and creates variations.
 *
 * @param count Number of colors to generate
 * @param baseHue Starting hue (0-360), defaults to random
 * @param hueVariation How much the hue can vary from base
 * @return List of coordinated colors
 */
@Composable
fun generateHarmonizedColors(
    count: Int,
    baseHue: Float = Random.nextFloat() * 360f,
    hueVariation: Float = 30f
): List<Color> {
    return remember {
        List(count) { index ->
            // Calculate hue with variations
            val hueStep = (360f / count)
            val hue = (baseHue + index * hueStep) % 360f

            // Slight random variations for saturation and brightness
            val saturation = 0.7f + Random.nextFloat() * 0.2f
            val brightness = 0.6f + Random.nextFloat() * 0.2f

            Color.hsl(hue, saturation, brightness)
        }
    }
}
