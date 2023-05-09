package com.girlify.dolarapp.dolar.data.model

import com.girlify.dolarapp.dolar.ui.model.DollarModel
import com.google.gson.annotations.SerializedName

data class DollarResponse (
    @SerializedName("compra") val compra: String,
    @SerializedName("venta") val venta: String,
    @SerializedName("fecha") val fecha: String,
    @SerializedName("variacion") val variacion: String,
    @SerializedName("class-variacion") val class_variacion: String,
    @SerializedName("valor_cierre_ant") val valor_cierre_ant: String
) {
    fun toDomain(name: String): DollarModel {
        return DollarModel(
            name,
            this.compra,
            this.venta,
            this.variacion,
            this.class_variacion,
        )
    }
}