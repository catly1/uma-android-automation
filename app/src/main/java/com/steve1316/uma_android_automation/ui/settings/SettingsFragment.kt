package com.steve1316.uma_android_automation.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.core.content.edit
import androidx.navigation.fragment.findNavController
import androidx.preference.*
import com.steve1316.uma_android_automation.MainActivity
import com.steve1316.uma_android_automation.R

class SettingsFragment : PreferenceFragmentCompat() {
	private val logTag: String = "[${MainActivity.loggerTag}]SettingsFragment"

	companion object {
		// Helper to get short month names, you can adjust as needed
		private val monthMap = mapOf(
			1 to "Jan", 2 to "Feb", 3 to "Mar", 4 to "Apr", 5 to "May", 6 to "Jun",
			7 to "Jul", 8 to "Aug", 9 to "Sep", 10 to "Oct", 11 to "Nov", 12 to "Dec"
		)

		// Data derived from Game.kt's raceDates
		val racePrefEntries: Array<CharSequence> = arrayOf(
			"Asahi Hai Futurity Stakes - Y1 Early Dec T23",
			"Hanshin Juvenile Fillies - Y1 Early Dec T23",
			"Hopeful Stakes - Y1 Late Dec T24",
			"Osaka Hai - Y2 Early Apr T31",
			"Satsuki Sho - Y2 Late Apr T32",
			"NHK Mile Cup - Y2 Early May T33",
			"Japanese Oaks - Y2 Late May T34",
			"Tokyo Yushun (Japanese Derby) - Y2 Late May T34",
			"Yasuda Kinen - Y2 Early Jun T35",
			"Takarazuka Kinen - Y2 Late Jun T36",
			"Japan Dirt Derby - Y2 Early Jul T37",
			"Sprinters Stakes - Y2 Early Oct T43",
			"Shuka Sho - Y2 Late Oct T44",
			"Kikuka Sho - Y2 Late Oct T44",
			"Tenno Sho (Autumn) - Y2 Late Oct T44",
			"JBC Classic - Y2 Early Nov T45",
			"JBC Ladies' Classic - Y2 Early Nov T45",
			"JBC Sprint - Y2 Early Nov T45",
			"Queen Elizabeth II Cup - Y2 Late Nov T46",
			"Japan Cup - Y2 Late Nov T46",
			"Mile Championship - Y2 Late Nov T46",
			"Champions Cup - Y2 Early Dec T47",
			"Arima Kinen - Y2 Late Dec T48",
			"Tokyo Daishoten - Y2 Late Dec T48",
			"February Stakes - Y3 Late Feb T52",
			"Osaka Hai - Y3 Late Mar T54",
			"Takamatsunomiya Kinen - Y3 Late Mar T54",
			"Satsuki Sho (Spring) - Y3 Late Apr T56",
			"Victoria Mile - Y3 Early May T57",
			"Yushun Himba - Y3 Late May T58",
			"Takarazuka Kinen - Y3 Late Jun T60",
			"Tenno Sho - Y3 Late Jun T60",
			"Sprinters Stakes - Y3 Early Oct T67",
			"Tenno Sho (Autumn) - Y3 Late Oct T68",
			"JBC Classic - Y3 Early Nov T69",
			"JBC Ladies' Classic - Y3 Early Nov T69",
			"JBC Sprint - Y3 Early Nov T69",
			"Queen Elizabeth II Cup - Y3 Late Nov T70",
			"Japan Cup - Y3 Late Nov T70",
			"Mile Championship - Y3 Late Nov T70",
			"Champions Cup - Y3 Early Dec T71",
			"Arima Kinen - Y3 Late Dec T72",
			"Tokyo Daishoten - Y3 Late Dec T72"
		)

		val racePrefEntryValues: Array<CharSequence> = arrayOf(
			"0:1,Early,12,23", // Asahi Hai Futurity Stakes
			"1:1,Early,12,23", // Hanshin Juvenile Fillies
			"2:1,Late,12,24",  // Hopeful Stakes
			"3:2,Early,4,31",  // Osaka Hai
			"4:2,Late,4,32",   // Satsuki Sho
			"5:2,Early,5,33",  // NHK Mile Cup
			"6:2,Late,5,34",   // Japanese Oaks
			"7:2,Late,5,34",   // Tokyo Yushun (Japanese Derby)
			"8:2,Early,6,35",  // Yasuda Kinen
			"9:2,Late,6,36",   // Takarazuka Kinen
			"10:2,Early,7,37", // Japan Dirt Derby
			"11:2,Early,10,43",// Sprinters Stakes
			"12:2,Late,10,44", // Shuka Sho
			"13:2,Late,10,44", // Kikuka Sho
			"14:2,Late,10,44", // Tenno Sho (Autumn)
			"15:2,Early,11,45",// JBC Classic
			"16:2,Early,11,45",// JBC Ladies' Classic
			"17:2,Early,11,45",// JBC Sprint
			"18:2,Late,11,46", // Queen Elizabeth II Cup
			"19:2,Late,11,46", // Japan Cup
			"20:2,Late,11,46", // Mile Championship
			"21:2,Early,12,47",// Champions Cup
			"22:2,Late,12,48", // Arima Kinen
			"23:2,Late,12,48", // Tokyo Daishoten
			"24:3,Late,2,52",  // February Stakes
			"25:3,Late,3,54",  // Osaka Hai
			"26:3,Late,3,54",  // Takamatsunomiya Kinen
			"27:3,Late,4,56",  // Satsuki Sho (Spring)
			"28:3,Early,5,57", // Victoria Mile
			"29:3,Late,5,58",  // Yushun Himba
			"30:3,Late,6,60",  // Takarazuka Kinen
			"31:3,Late,6,60",  // Tenno Sho
			"32:3,Early,10,67",// Sprinters Stakes
			"33:3,Late,10,68", // Tenno Sho (Autumn)
			"34:3,Early,11,69",// JBC Classic
			"35:3,Early,11,69",// JBC Ladies' Classic
			"36:3,Early,11,69",// JBC Sprint
			"37:3,Late,11,70", // Queen Elizabeth II Cup
			"38:3,Late,11,70", // Japan Cup
			"39:3,Late,11,70", // Mile Championship
			"40:3,Early,12,71",// Champions Cup
			"41:3,Late,12,72", // Arima Kinen
			"42:3,Late,12,72"  // Tokyo Daishoten
		)
	}
	private lateinit var sharedPreferences: SharedPreferences
	
	// This listener is triggered whenever the user changes a Preference setting in the Settings Page.
	private val onSharedPreferenceChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
		if (key != null) {
			when (key) {
				"campaign" -> {
					val campaignListPreference = findPreference<ListPreference>("campaign")!!
					campaignListPreference.summary = "Selected: ${campaignListPreference.value}"
					sharedPreferences.edit {
						putString("campaign", campaignListPreference.value)
						commit()
					}
				}
				"enableFarmingFans" -> {
					val enableFarmingFansPreference = findPreference<CheckBoxPreference>("enableFarmingFans")!!
					val daysToRunExtraRacesPreference = findPreference<SeekBarPreference>("daysToRunExtraRaces")!!
					
					daysToRunExtraRacesPreference.isEnabled = enableFarmingFansPreference.isChecked
					
					sharedPreferences.edit {
						putBoolean("enableFarmingFans", enableFarmingFansPreference.isChecked)
						commit()
					}
				}
				"daysToRunExtraRaces" -> {
					val daysToRunExtraRacesPreference = findPreference<SeekBarPreference>("daysToRunExtraRaces")!!
					
					sharedPreferences.edit {
						putInt("daysToRunExtraRaces", daysToRunExtraRacesPreference.value)
						commit()
					}
				}
				"enableSkillPointCheck" -> {
					val enableSkillPointCheckPreference = findPreference<CheckBoxPreference>("enableSkillPointCheck")!!
					val skillPointCheckPreference = findPreference<SeekBarPreference>("skillPointCheck")!!
					skillPointCheckPreference.isEnabled = enableSkillPointCheckPreference.isChecked
					
					sharedPreferences.edit {
						putBoolean("enableSkillPointCheck", enableSkillPointCheckPreference.isChecked)
						commit()
					}
				}
				"skillPointCheck" -> {
					val skillPointCheckPreference = findPreference<SeekBarPreference>("skillPointCheck")!!
					
					sharedPreferences.edit {
						putInt("skillPointCheck", skillPointCheckPreference.value)
						commit()
					}
				}
				"enablePopupCheck" -> {
					val enablePopupCheckPreference = findPreference<CheckBoxPreference>("enablePopupCheck")!!
					
					sharedPreferences.edit {
						putBoolean("enablePopupCheck", enablePopupCheckPreference.isChecked)
						commit()
					}
				}
				"disableRaceRetries" -> {
					val disableRaceRetriesPreference = findPreference<CheckBoxPreference>("disableRaceRetries")!!

					sharedPreferences.edit {
						putBoolean("disableRaceRetries", disableRaceRetriesPreference.isChecked)
						commit()
					}
				}
				"enableStopOnMandatoryRace" -> {
					val enableStopOnMandatoryRacePreference = findPreference<CheckBoxPreference>("enableStopOnMandatoryRace")!!
					
					sharedPreferences.edit {
						putBoolean("enableStopOnMandatoryRace", enableStopOnMandatoryRacePreference.isChecked)
						commit()
					}
				}
				"enablePrioritizeEnergyOptions" -> {
					val enablePrioritizeEnergyOptionsPreference = findPreference<CheckBoxPreference>("enablePrioritizeEnergyOptions")!!

					sharedPreferences.edit {
						putBoolean("enablePrioritizeEnergyOptions", enablePrioritizeEnergyOptionsPreference.isChecked)
						commit()
					}
				}
				"enableForceRacing" -> {
					val enableForceRacingPreference = findPreference<CheckBoxPreference>("enableForceRacing")!!

					sharedPreferences.edit {
						putBoolean("enableForceRacing", enableForceRacingPreference.isChecked)
						commit()
					}
				}
				"debugMode" -> {
					val debugModePreference = findPreference<CheckBoxPreference>("debugMode")!!
					
					sharedPreferences.edit {
						putBoolean("debugMode", debugModePreference.isChecked)
						commit()
					}
				}
				"confidence" -> {
					val confidencePreference = findPreference<SeekBarPreference>("confidence")!!

					sharedPreferences.edit {
						putInt("confidence", confidencePreference.value)
						commit()
					}
				}
				"customScale" -> {
					val customScalePreference = findPreference<SeekBarPreference>("customScale")!!

					sharedPreferences.edit {
						putInt("customScale", customScalePreference.value)
						commit()
					}
				}
				"debugMode_startTemplateMatchingTest" -> {
					val debugModeStartTemplateMatchingTestPreference = findPreference<CheckBoxPreference>("debugMode_startTemplateMatchingTest")!!

					sharedPreferences.edit {
						putBoolean("debugMode_startTemplateMatchingTest", debugModeStartTemplateMatchingTestPreference.isChecked)
						commit()
					}
				}
				"debugMode_startSingleTrainingFailureOCRTest" -> {
					val debugModeStartSingleTrainingFailureOCRTestPreference = findPreference<CheckBoxPreference>("debugMode_startSingleTrainingFailureOCRTest")!!

					sharedPreferences.edit {
						putBoolean("debugMode_startSingleTrainingFailureOCRTest", debugModeStartSingleTrainingFailureOCRTestPreference.isChecked)
						commit()
					}
				}
				"debugMode_startComprehensiveTrainingFailureOCRTest" -> {
					val debugModeStartComprehensiveTrainingFailureOCRTestPreference = findPreference<CheckBoxPreference>("debugMode_startComprehensiveTrainingFailureOCRTest")!!

					sharedPreferences.edit {
						putBoolean("debugMode_startComprehensiveTrainingFailureOCRTest", debugModeStartComprehensiveTrainingFailureOCRTestPreference.isChecked)
						commit()
					}
				}
				"hideComparisonResults" -> {
					val hideComparisonResultsPreference = findPreference<CheckBoxPreference>("hideComparisonResults")!!
					
					sharedPreferences.edit {
						putBoolean("hideComparisonResults", hideComparisonResultsPreference.isChecked)
						commit()
					}
				}
                "userDefinedSpecialRaces" -> {
                    val userDefinedSpecialRacesPreference = findPreference<MultiSelectListPreference>("userDefinedSpecialRaces")!!
                    val selectedValues = userDefinedSpecialRacesPreference.values

                    // Update summary with human-readable entries
                    val currentEntries = userDefinedSpecialRacesPreference.entries ?: SettingsFragment.racePrefEntries
                    val currentEntryValues = userDefinedSpecialRacesPreference.entryValues ?: SettingsFragment.racePrefEntryValues
                    val selectedDisplayEntries = selectedValues.mapNotNull { value ->
                        val index = currentEntryValues.indexOf(value)
                        if (index >= 0 && index < currentEntries.size) currentEntries[index] else null
                    }
                    userDefinedSpecialRacesPreference.summary = "Races: ${if (selectedDisplayEntries.isNotEmpty()) selectedDisplayEntries.joinToString(", ") else "None"}"
                    
                    sharedPreferences.edit {
                        putStringSet("userDefinedSpecialRaces", selectedValues)
                        commit()
                    }
                }
			}
		}
	}
	
	override fun onResume() {
		super.onResume()
		
		// Makes sure that OnSharedPreferenceChangeListener works properly and avoids the situation where the app suddenly stops triggering the listener.
		preferenceScreen.sharedPreferences?.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener)
	}
	
	override fun onPause() {
		super.onPause()
		preferenceScreen.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener)
	}
	
	// This function is called right after the user navigates to the SettingsFragment.
	override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    val defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
    try {
        // Attempt to read "userDefinedSpecialRaces". If it's stored as a String (or any other non-Set type), 
        // getStringSet will throw a ClassCastException.
        // If it's already a Set or if the key doesn't exist (returns null), no exception is thrown.
        defaultSharedPreferences.getStringSet("userDefinedSpecialRaces", null)
    } catch (e: ClassCastException) {
        // The stored type is incompatible (e.g., it's a String from an older version).
        Log.w(logTag, "Incompatible value type for 'userDefinedSpecialRaces' in SharedPreferences. Clearing to prevent crash. User selection for this preference will be reset.", e)
        defaultSharedPreferences.edit().remove("userDefinedSpecialRaces").apply()
    }
    
		// Display the layout using the preferences xml.
		setPreferencesFromResource(R.xml.preferences, rootKey)
		
		// Get the SharedPreferences.
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
		
		// Grab the saved preferences from the previous time the user used the app.
		val campaign: String = sharedPreferences.getString("campaign", "")!!
		val enableFarmingFans: Boolean = sharedPreferences.getBoolean("enableFarmingFans", false)
		val daysToRunExtraRaces: Int = sharedPreferences.getInt("daysToRunExtraRaces", 4)
		val enableSkillPointCheck: Boolean = sharedPreferences.getBoolean("enableSkillPointCheck", false)
		val skillPointCheck: Int = sharedPreferences.getInt("skillPointCheck", 750)
		val enablePopupCheck: Boolean = sharedPreferences.getBoolean("enablePopupCheck", false)
		val disableRaceRetries: Boolean = sharedPreferences.getBoolean("disableRaceRetries", false)
		val enableStopOnMandatoryRace: Boolean = sharedPreferences.getBoolean("enableStopOnMandatoryRace", false)
		val enablePrioritizeEnergyOptions: Boolean = sharedPreferences.getBoolean("enablePrioritizeEnergyOptions", false)
		val enableForceRacing: Boolean = sharedPreferences.getBoolean("enableForceRacing", false)
		val debugMode: Boolean = sharedPreferences.getBoolean("debugMode", false)
		val confidence: Int = sharedPreferences.getInt("confidence", 80)
		val customScale: Int = sharedPreferences.getInt("customScale", 100)
		val debugModeStartTemplateMatchingTest: Boolean = sharedPreferences.getBoolean("debugMode_startTemplateMatchingTest", false)
		val debugModeStartSingleTrainingFailureOCRTest: Boolean = sharedPreferences.getBoolean("debugMode_startSingleTrainingFailureOCRTest", false)
		val debugModeStartComprehensiveTrainingFailureOCRTest: Boolean = sharedPreferences.getBoolean("debugMode_startComprehensiveTrainingFailureOCRTest", false)
		val hideComparisonResults: Boolean = sharedPreferences.getBoolean("hideComparisonResults", true)
		val userDefinedSpecialRacesValues: Set<String> = sharedPreferences.getStringSet("userDefinedSpecialRaces", emptySet())!! // Renamed for clarity
		
		// Get references to the Preference components.
		val campaignListPreference = findPreference<ListPreference>("campaign")!!
		val enableFarmingFansPreference = findPreference<CheckBoxPreference>("enableFarmingFans")!!
		val daysToRunExtraRacesPreference = findPreference<SeekBarPreference>("daysToRunExtraRaces")!!
		val enableSkillPointCheckPreference = findPreference<CheckBoxPreference>("enableSkillPointCheck")!!
		val skillPointCheckPreference = findPreference<SeekBarPreference>("skillPointCheck")!!
		val enablePopupCheckPreference = findPreference<CheckBoxPreference>("enablePopupCheck")!!
		val disableRaceRetriesPreference = findPreference<CheckBoxPreference>("disableRaceRetries")!!
		val enableStopOnMandatoryRacePreference = findPreference<CheckBoxPreference>("enableStopOnMandatoryRace")!!
		val enablePrioritizeEnergyOptionsPreference = findPreference<CheckBoxPreference>("enablePrioritizeEnergyOptions")!!
		val enableForceRacingPreference = findPreference<CheckBoxPreference>("enableForceRacing")!!
		val debugModePreference = findPreference<CheckBoxPreference>("debugMode")!!
		val confidencePreference = findPreference<SeekBarPreference>("confidence")!!
		val customScalePreference = findPreference<SeekBarPreference>("customScale")!!
		val debugModeStartTemplateMatchingTestPreference = findPreference<CheckBoxPreference>("debugMode_startTemplateMatchingTest")!!
		val debugModeStartSingleTrainingFailureOCRTestPreference = findPreference<CheckBoxPreference>("debugMode_startSingleTrainingFailureOCRTest")!!
		val debugModeStartComprehensiveTrainingFailureOCRTestPreference = findPreference<CheckBoxPreference>("debugMode_startComprehensiveTrainingFailureOCRTest")!!
		val hideComparisonResultsPreference = findPreference<CheckBoxPreference>("hideComparisonResults")!!
		val userDefinedSpecialRacesPreference = findPreference<MultiSelectListPreference>("userDefinedSpecialRaces")!! // Changed type
		
		// Now set the following values from the shared preferences.
		campaignListPreference.value = campaign
		if (campaign.isNotEmpty()) { // Changed from campaign != "" for idiomatic Kotlin
			campaignListPreference.summary = "Selected: ${campaignListPreference.value}"
		}
		enableFarmingFansPreference.isChecked = enableFarmingFans
		daysToRunExtraRacesPreference.isEnabled = enableFarmingFansPreference.isChecked
		daysToRunExtraRacesPreference.value = daysToRunExtraRaces
		enableSkillPointCheckPreference.isChecked = enableSkillPointCheck
		skillPointCheckPreference.value = skillPointCheck
		enablePopupCheckPreference.isChecked = enablePopupCheck
		disableRaceRetriesPreference.isChecked = disableRaceRetries
		enableStopOnMandatoryRacePreference.isChecked = enableStopOnMandatoryRace
		enablePrioritizeEnergyOptionsPreference.isChecked = enablePrioritizeEnergyOptions
		enableForceRacingPreference.isChecked = enableForceRacing
		debugModePreference.isChecked = debugMode
		confidencePreference.value = confidence
		customScalePreference.value = customScale
		debugModeStartTemplateMatchingTestPreference.isChecked = debugModeStartTemplateMatchingTest
		debugModeStartSingleTrainingFailureOCRTestPreference.isChecked = debugModeStartSingleTrainingFailureOCRTest
		debugModeStartComprehensiveTrainingFailureOCRTestPreference.isChecked = debugModeStartComprehensiveTrainingFailureOCRTest
		hideComparisonResultsPreference.isChecked = hideComparisonResults
		skillPointCheckPreference.isEnabled = enableSkillPointCheckPreference.isChecked
		
		// Setup for UserDefinedSpecialRaces MultiSelectListPreference
		userDefinedSpecialRacesPreference.entries = SettingsFragment.racePrefEntries
		userDefinedSpecialRacesPreference.entryValues = SettingsFragment.racePrefEntryValues
		userDefinedSpecialRacesPreference.values = userDefinedSpecialRacesValues
		// Update summary with human-readable entries
		val selectedEntriesForSummary = userDefinedSpecialRacesValues.mapNotNull { value ->
			val index = SettingsFragment.racePrefEntryValues.indexOf(value)
			if (index >= 0) SettingsFragment.racePrefEntries[index] else null
		}
		userDefinedSpecialRacesPreference.summary = "Races: ${if (selectedEntriesForSummary.isNotEmpty()) selectedEntriesForSummary.joinToString(", ") else "None"}"
		
		// Solution courtesy of https://stackoverflow.com/a/63368599
		// In short, Fragments via the mobile_navigation.xml are children of NavHostFragment, not MainActivity's supportFragmentManager.
		// This is why using the method described in official Google docs via OnPreferenceStartFragmentCallback and using the supportFragmentManager is not correct for this instance.
		findPreference<Preference>("trainingOptions")?.setOnPreferenceClickListener {
			// Navigate to the TrainingFragment.
			findNavController().navigate(R.id.nav_training)
			true
		}
		findPreference<Preference>("trainingEventOptions")?.setOnPreferenceClickListener {
			// Navigate to the TrainingEventFragment.
			findNavController().navigate(R.id.nav_training_event)
			true
		}
		findPreference<Preference>("ocrOptions")?.setOnPreferenceClickListener {
			// Navigate to the OCRFragment.
			findNavController().navigate(R.id.nav_ocr)
			true
		}
		
		Log.d(logTag, "Main Preferences created successfully.")
	}
}
