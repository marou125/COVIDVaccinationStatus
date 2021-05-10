package com.marou125.covidvaccinationstatus.service

import com.google.gson.annotations.SerializedName

class CaseData (var All: CaseInfo) {
}

class CaseInfo(var confirmed: Int, var recovered: Int, var deaths: Int,
               var country: String, var dates: HashMap<String, Int>)

class JsonCaseData(var Afghanistan: CaseData,
                   var Albania: CaseData,
                   var Algeria: CaseData,
                   @SerializedName("American Samoa")
                   var AmericanSamoa: CaseData,
                   var Andorra: CaseData,
                   var Angola: CaseData,
                   var Anguilla: CaseData,
                   var Antarctica: CaseData,
                   @SerializedName("Antigua and Barbuda")
                   var AntiguaAndBarbuda: CaseData,
                   var Argentina: CaseData,
                   var Armenia: CaseData,
                   var Aruba: CaseData,
                   var Australia: CaseData,
                   var Austria: CaseData,
                   var Azerbaijan: CaseData,
                   var Bahamas: CaseData,
                   var Bahrain: CaseData,
                   var Bangladesh: CaseData,
                   var Barbados: CaseData,
                   var Belarus: CaseData,
                   var Belgium: CaseData,
                   var Belize: CaseData,
                   var Benin: CaseData,
                   var Bermuda: CaseData,
                   var Bhutan: CaseData,
                   var Bolivia: CaseData,
                   @SerializedName("Bosnia and Herzegovina")
                   var BosniaAndHerzegovina: CaseData,
                   var Botswana: CaseData,
                   @SerializedName("Bouvet Island")
                   var BouvetIsland: CaseData,
                   var Brazil: CaseData,
                   @SerializedName("British Indian Ocean Territory")
                   var BritishIndianOceanTerritory: CaseData,
                   @SerializedName("Brunei Darussalam")
                   var BruneiDarussalam: CaseData,
                   var Bulgaria: CaseData,
                   @SerializedName("Burkina Faso")
                   var BurkinaFaso: CaseData,
                   var Burundi: CaseData,
                   var Cambodia: CaseData,
                   var Cameroon: CaseData,
                   var Canada: CaseData,
                   @SerializedName("Cape Verde")
                   var CapeVerde: CaseData,
                   @SerializedName("Cayman Islands")
                   var CaymanIslands: CaseData,
                   @SerializedName("Central African Republic")
                   var CentralAfricanRepublic: CaseData,
                   var Chad: CaseData,
                   var Chile: CaseData,
                   var China: CaseData,
                   @SerializedName("Christmas Island")
                   var ChristmasIsland: CaseData,
                   @SerializedName("Cocos (Keeling) Islands")
                   var Cocos: CaseData,
                   var Colombia: CaseData,
                   var Comoros: CaseData,
                   var Congo: CaseData,
                   @SerializedName("Congo, the Democratic Republic of the")
                   var CongoDR: CaseData,
                   @SerializedName("Cook Islands")
                   var CookIslands: CaseData,
                   @SerializedName("Costa Rica")
                   var CostaRica: CaseData,
                   @SerializedName("Cote d'Ivoire")
                   var CoteDIvoire: CaseData,
                   var Croatia: CaseData,
                   var Cuba: CaseData,
                   var Cyprus: CaseData,
                   @SerializedName("Czechia")
                   var CzechRepublic: CaseData,
                   var Denmark: CaseData,
                   var Djibouti: CaseData,
                   var Dominica: CaseData,
                   @SerializedName("Dominican Republic")
                   var DominicanRepublic: CaseData,
                   @SerializedName("East Timor")
                   var EastTimor: CaseData,
                   var Ecuador: CaseData,
                   var Egypt: CaseData,
                   @SerializedName("El Salvador")
                   var ElSalvador: CaseData,
                   @SerializedName("Equatorial Guinea")
                   var EquatorialGuinea: CaseData,
                   var Eritrea: CaseData,
                   var Estonia: CaseData,
                   var Ethiopia: CaseData,
                   @SerializedName("Falkland Islands (Malvinas)")
                   var FalklandIslands: CaseData,
                   @SerializedName("Faroe Islands")
                   var FaroeIslands: CaseData,
                   var Fiji: CaseData,
                   var Finland: CaseData,
                   var France: CaseData,
                   @SerializedName("France Metropolitan")
                   var FranceMetropolitan: CaseData,
                   @SerializedName("French Guiana")
                   var FrenchGuiana: CaseData,
                   @SerializedName("French Polynesia")
                   var FrenchPolynesia: CaseData,
                   @SerializedName("French Southern Territories")
                   var FrenchSouthernTerritories: CaseData,
                   var Gabon: CaseData,
                   var Gambia: CaseData,
                   var Georgia: CaseData,
                   var Germany: CaseData,
                   var Ghana: CaseData,
                   var Gibraltar: CaseData,
                   var Greece: CaseData,
                   var Greenland: CaseData,
                   var Grenada: CaseData,
                   var Guadeloupe: CaseData,
                   var Guam: CaseData,
                   var Guatemala: CaseData,
                   var Guinea: CaseData,
                   @SerializedName("Guinea-Bissau")
                   var GuineaBissau: CaseData,
                   var Guyana: CaseData,
                   var Haiti: CaseData,
                   @SerializedName("Heard and Mc Donald Islands")
                   var HeardAndMcDonaldIslands: CaseData,
                   @SerializedName("Holy See (Vatican City State)")
                   var HolySee: CaseData,
                   var Honduras: CaseData,
                   @SerializedName("Hong Kong")
                   var HongKong: CaseData,
                   var Hungary: CaseData,
                   var Iceland: CaseData,
                   var India: CaseData,
                   var Indonesia: CaseData,
                   @SerializedName("Iran (Islamic Republic of)")
                   var Iran: CaseData,
                   var Iraq: CaseData,
                   var Ireland: CaseData,
                   var Israel: CaseData,
                   var Italy: CaseData,
                   var Jamaica: CaseData,
                   var Japan: CaseData,
                   var Jordan: CaseData,
                   var Kazakhstan: CaseData,
                   var Kenya: CaseData,
                   var Kiribati: CaseData,
                   @SerializedName("Korea, Democratic People's Republic of")
                   var NorthKorea: CaseData,
                   @SerializedName("Korea, Republic of")
                   var SouthKorea: CaseData,
                   var Kuwait: CaseData,
                   var Kyrgyzstan: CaseData,
                   @SerializedName("Lao, People's Democratic Republic")
                   var Lao: CaseData,
                   var Latvia: CaseData,
                   var Lebanon: CaseData,
                   var Lesotho: CaseData,
                   var Liberia: CaseData,
                   @SerializedName("Libyan Arab Jamahiriya")
                   var LibyanArabJamahiriya: CaseData,
                   var Liechtenstein: CaseData,
                   var Lithuania: CaseData,
                   var Luxembourg: CaseData,
                   var Macau: CaseData,
                   @SerializedName("Macedonia, The Former Yugoslav Republic of")
                   var Macedonia: CaseData,
                   var Madagascar: CaseData,
                   var Malawi: CaseData,
                   var Malaysia: CaseData,
                   var Maldives: CaseData,
                   var Mali: CaseData,
                   var Malta: CaseData,
                   @SerializedName("Marshall Islands")
                   var MarshallIslands: CaseData,
                   var Martinique: CaseData,
                   var Mauritania: CaseData,
                   var Mauritius: CaseData,
                   var Mayotte: CaseData,
                   var Mexico: CaseData,
                   @SerializedName("Micronesia, Federated States of")
                   var Micronesia: CaseData,
                   @SerializedName("Moldova, Republic of")
                   var Moldova: CaseData,
                   var Monaco: CaseData,
                   var Mongolia: CaseData,
                   var Montserrat: CaseData,
                   var Morocco: CaseData,
                   var Mozambique: CaseData,
                   var Myanmar: CaseData,
                   var Namibia: CaseData,
                   var Nauru: CaseData,
                   var Nepal: CaseData,
                   var Netherlands: CaseData,
                   @SerializedName("Netherlands Antilles")
                   var NetherlandsAntilles: CaseData,
                   @SerializedName("New Caledonia")
                   var NewCaledonia: CaseData,
                   @SerializedName("New Zealand")
                   var NewZealand: CaseData,
                   var Nicaragua: CaseData,
                   var Niger: CaseData,
                   var Nigeria: CaseData,
                   var Niue: CaseData,
                   @SerializedName("Norfolk Island")
                   var NorfolkIsland: CaseData,
                   @SerializedName("Northern Mariana Islands")
                   var NorthernMarianaIslands: CaseData,
                   var Norway: CaseData,
                   var Oman: CaseData,
                   var Pakistan: CaseData,
                   var Palau: CaseData,
                   var Panama: CaseData,
                   @SerializedName("Papua New Guinea")
                   var PapuaNewGuinea: CaseData,
                   var Paraguay: CaseData,
                   var Peru: CaseData,
                   var Philippines: CaseData,
                   var Pitcairn: CaseData,
                   var Poland: CaseData,
                   var Portugal: CaseData,
                   @SerializedName("Puerto Rico")
                   var PuertoRico: CaseData,
                   var Qatar: CaseData,
                   var Reunion: CaseData,
                   var Romania: CaseData,
                   @SerializedName("Russian Federation")
                   var RussianFederation: CaseData,
                   var Rwanda: CaseData,
                   @SerializedName("Saint Kitts and Nevis")
                   var SaintKittsAndNevis: CaseData,
                   @SerializedName("Saint Lucia")
                   var SaintLucia: CaseData,
                   @SerializedName("Saint Vincent and the Grenadines")
                   var SaintVincentAndTheGrenadines: CaseData,
                   var Samoa: CaseData,
                   @SerializedName("San Marino")
                   var SanMarino: CaseData,
                   @SerializedName("Sao Tome and Principe")
                   var SaoTomeAndPrincipe: CaseData,
                   @SerializedName("Saudi Arabia")
                   var SaudiArabia: CaseData,
                   var Senegal: CaseData,
                   var Seychelles: CaseData,
                   @SerializedName("Sierra Leone")
                   var SierraLeone: CaseData,
                   var Singapore: CaseData,
                   @SerializedName("Slovakia (Slovak Republic)")
                   var Slovakia: CaseData,
                   var Slovenia: CaseData,
                   @SerializedName("Solomon Islands")
                   var SolomonIslands: CaseData,
                   var Somalia: CaseData,
                   @SerializedName("South Africa")
                   var SouthAfrica: CaseData,
                   @SerializedName("South Georgia and the South Sandwich Islands")
                   var SouthGeorgiaAndTheSouthSandwichIslands: CaseData,
                   var Spain: CaseData,
                   @SerializedName("Sri Lanka")
                   var SriLanka: CaseData,
                   @SerializedName("St. Helena")
                   var StHelena: CaseData,
                   @SerializedName("St. Pierre and Miquelon")
                   var StPierreAndMiquelon: CaseData,
                   var Sudan: CaseData,
                   var Suriname: CaseData,
                   @SerializedName("Svalbard and Jan Mayen Islands")
                   var SvalbardAndJanMayenIslands: CaseData,
                   var Swaziland: CaseData,
                   var Sweden: CaseData,
                   var Switzerland: CaseData,
                   @SerializedName("Syrian Arab Republic")
                   var SyrianArabRepublic: CaseData,
                   @SerializedName("Taiwan, Province of China")
                   var Taiwan: CaseData,
                   var Tajikistan: CaseData,
                   @SerializedName("Tanzania, United Republic of")
                   var Tanzania: CaseData,
                   var Thailand: CaseData,
                   var Togo: CaseData,
                   var Tokelau: CaseData,
                   var Tonga: CaseData,
                   @SerializedName("Trinidad and Tobago")
                   var TrinidadAndTobago: CaseData,
                   var Tunisia: CaseData,
                   var Turkey: CaseData,
                   var Turkmenistan: CaseData,
                   @SerializedName("Turks and Caicos Islands")
                   var TurksAndCaicosIslands: CaseData,
                   var Tuvalu: CaseData,
                   var Uganda: CaseData,
                   var Ukraine: CaseData,
                   @SerializedName("United Arab Emirates")
                   var UnitedArabEmirates: CaseData,
                   @SerializedName("United Kingdom")
                   var UnitedKingdom: CaseData,
                   @SerializedName("United States")
                   var UnitedStates: CaseData,
                   @SerializedName("United States Minor Outlying Islands")
                   var UnitedStatesMinorOutlyingIslands: CaseData,
                   var Uruguay: CaseData,
                   var Uzbekistan: CaseData,
                   var Vanuatu: CaseData,
                   var Venezuela: CaseData,
                   var Vietnam: CaseData,
                   @SerializedName("Virgin Islands (British)")
                   var VirginIslandsUK: CaseData,
                   @SerializedName("Virgin Islands (U.S.)")
                   var VirginIslandsUS: CaseData,
                   @SerializedName("Wallis and Futuna Islands")
                   var WallisAndFutunaIslands: CaseData,
                   @SerializedName("Western Sahara")
                   var WesternSahara: CaseData,
                   var Yemen: CaseData,
                   var Yugoslavia: CaseData,
                   var Zambia: CaseData,
                   var Zimbabwe: CaseData,
                   var Palestine: CaseData
)