package com.ipmus.entities

import jetbrains.exodus.entitystore.Entity
import java.time.LocalDate
import jetbrains.exodus.bindings.StringBinding

/**
 * Created by mozturk on 7/26/2017.
 */

data class Item(val cancelled: Boolean, val date: LocalDate, val poNum: String,
                //val vendor: Vendor,
                //val design: DesignColor,
                val shippedYards: Double, val FOB: Int, val LDP: Int,
                //val customer: Customer,
                val customerPO: String, val millETS: LocalDate) {
                //val shipment: Shipment?) {
    constructor (entity: Entity) :
            this(cancelled = entity.getProperty("cancelled") == "true",
                    date = LocalDate.parse(entity.getProperty("date") as String),
                    poNum = entity.getProperty("poNum") as String,
                    shippedYards = (entity.getProperty("shippedYards") as String).toDouble(),
                    FOB = (entity.getProperty("FOB") as String).toInt(),
                    LDP = (entity.getProperty("LDP") as String).toInt(),
                    customerPO = entity.getProperty("customerPO") as String,
                    millETS = LocalDate.parse(entity.getProperty("millETS") as String)
                    )

}