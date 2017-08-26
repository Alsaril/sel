package models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Barcode(@SerializedName("barcode")
              @Expose
              val barcode: String)
