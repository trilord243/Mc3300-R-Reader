package live.trilord.rfid_app

import android.media.ToneGenerator
import android.media.AudioManager


import android.content.Context
import android.util.Log
import android.util.Log.e
import com.zebra.rfid.api3.*

class RFIDManager(private val context: Context) {
    private val toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 100)

    private var readers: Readers? = null
    private var availableRFIDReaderList: ArrayList<ReaderDevice>? = null
    private var readerDevice: ReaderDevice? = null
    var reader: RFIDReader? = null
    private val TAG = "RFIDManager"
    private var isReading = false




    fun startInventory() {
        try {
            reader?.Actions?.Inventory?.perform()
            Log.d(TAG, "🔍 Inicio de lectura de etiquetas...")
        } catch (e: Exception) {
            Log.e(TAG, "Error al iniciar la lectura: ${e.localizedMessage}")
        }
    }

    fun stopInventory() {
        try {
            reader?.Actions?.Inventory?.stop()
            Log.d(TAG, "🛑 Lectura detenida.")
        } catch (e: Exception) {
            Log.e(TAG, "Error al detener la lectura: ${e.localizedMessage}")
        }
    }

    fun initializeReader(): Boolean {
        try {
            if (readers == null) {
                readers = Readers(context, ENUM_TRANSPORT.SERVICE_SERIAL)
            }

            availableRFIDReaderList = readers?.GetAvailableRFIDReaderList()

            if (!availableRFIDReaderList.isNullOrEmpty()) {
                readerDevice = availableRFIDReaderList!![0]
                reader = readerDevice?.rfidReader
                if (reader != null && !reader!!.isConnected) {
                    reader!!.connect()
                    reader?.Config?.beeperVolume = BEEPER_VOLUME.HIGH_BEEP // Activar sonido
                    configureReader()
                    return true
                }
            }
        } catch (e: InvalidUsageException) {
            Log.e(TAG, "Invalid Usage: ${e.localizedMessage}")
        } catch (e: OperationFailureException) {
            Log.e(TAG, "Operation Failure: ${e.localizedMessage}")
        }
        return false
    }

    fun switchTriggerMode(isRFID: Boolean) {
        try {
            if (isRFID) {
                reader?.Config?.setTriggerMode(ENUM_TRIGGER_MODE.RFID_MODE, false)
                Log.d(TAG, "Trigger Mode cambiado a RFID")
            } else {
                reader?.Config?.setTriggerMode(ENUM_TRIGGER_MODE.BARCODE_MODE, false)
                Log.d(TAG, "Trigger Mode cambiado a BARCODE")
            }
        } catch (e: InvalidUsageException) {
            Log.e(TAG, "Error al cambiar Trigger Mode: ${e.localizedMessage}")
        } catch (e: OperationFailureException) {
            Log.e(TAG, "Operation Failure: ${e.localizedMessage}")
        }
    }

    private fun configureReader() {
        if (reader?.isConnected == true) {
            try {
                val triggerInfo = TriggerInfo()
                triggerInfo.StartTrigger.setTriggerType(START_TRIGGER_TYPE.START_TRIGGER_TYPE_IMMEDIATE)
                triggerInfo.StopTrigger.setTriggerType(STOP_TRIGGER_TYPE.STOP_TRIGGER_TYPE_IMMEDIATE)

                // Configurar eventos del lector
                reader?.Events?.apply {
                    setHandheldEvent(true)  // Detecta el gatillo físico
                    setTagReadEvent(true)   // Habilita la lectura de etiquetas
                    setAttachTagDataWithReadEvent(false)
                }

                // Registrar el listener de eventos
                reader?.Events?.addEventsListener(EventHandler())


                Log.d(TAG, "Gatillo configurado correctamente")
            } catch (e: InvalidUsageException) {
                Log.e(TAG, "Error configurando el lector: ${e.localizedMessage}")
            } catch (e: OperationFailureException) {
                Log.e(TAG, "Error de operación: ${e.localizedMessage}")
            }
        }
    }


      fun setTriggerFalse(){
        reader?.Events?.apply {
            setHandheldEvent(false)
            setTagReadEvent(false)   // Habilita la lectura de etiquetas
            setAttachTagDataWithReadEvent(true)
        }
    }



    fun startInventory(callback: (String) -> Unit) {
        try {
            reader?.Actions?.Inventory?.perform()
            Log.d(TAG, "Inventory started")

            val tags = reader?.Actions?.getReadTags(100)
            if (tags != null) {
                for (tag in tags) {
                    callback(tag.tagID)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error reading tags: ${e.localizedMessage}")
        }
    }

    fun disconnectReader() {
        try {
            reader?.disconnect()
            readers?.Dispose()
            Log.d(TAG, "Reader disconnected successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error disconnecting reader: ${e.localizedMessage}")
        }
    }

    fun writeTag(tagID: String, data: String): Boolean {
        return try {
            val tagAccess = TagAccess() // Crear instancia de TagAccess
            val writeAccessParams = tagAccess.WriteAccessParams() // Crear instancia desde TagAccess

            writeAccessParams.setAccessPassword(0) // Acceso sin contraseña
            writeAccessParams.setMemoryBank(MEMORY_BANK.MEMORY_BANK_EPC) // Escribir en EPC
            writeAccessParams.setOffset(2) // Posición de memoria donde escribir
            writeAccessParams.setWriteData(data) // Datos a escribir
            writeAccessParams.setWriteDataLength(data.length / 4) // Número de palabras a escribir

            // Realizar la escritura
            val tagData = TagData()
            reader?.Actions?.TagAccess?.writeWait(tagID, writeAccessParams, null, tagData)

            Log.d(TAG, "✅ Datos escritos en el tag: $data, Palabras escritas: ${tagData.numberOfWords}")
            true
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error escribiendo en el tag: ${e.localizedMessage}")
            false
        }
    }





    inner class EventHandler : RfidEventsListener {

        override fun eventReadNotify(e: RfidReadEvents?) {
            val tags = reader?.Actions?.getReadTags(100)
            if (tags != null) {
                for (tag in tags) {

                    Log.d(TAG, "Etiqueta detectada: ${tag.tagID}")

                    onTagReadCallback?.invoke(tag.tagID)
                    toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP, 150)
                }
            }
        }

        override fun eventStatusNotify(rfidStatusEvents: RfidStatusEvents?) {
            rfidStatusEvents?.StatusEventData?.let {
                when (it.statusEventType) {
                    STATUS_EVENT_TYPE.HANDHELD_TRIGGER_EVENT -> {
                        if (isReading) { // Solo lee si isReading es true
                            if (it.HandheldTriggerEventData.handheldEvent == HANDHELD_TRIGGER_EVENT_TYPE.HANDHELD_TRIGGER_PRESSED) {
                                startInventory()
                            } else if (it.HandheldTriggerEventData.handheldEvent == HANDHELD_TRIGGER_EVENT_TYPE.HANDHELD_TRIGGER_RELEASED) {
                                stopInventory()
                            } else {
                                Log.d(TAG, "Evento de gatillo desconocido")
                            }
                        } else {
                            Log.d(TAG, "Lectura deshabilitada, no se leerán etiquetas")
                        }
                    }
                    else -> Log.d(TAG, "Otro evento de estado detectado")
                }
            }
        }





    }
    fun enableTrigger(onTagRead: (String) -> Unit) {
        isReading = true
        onTagReadCallback = onTagRead // Guardar la referencia del callback
        Log.d(TAG, "📡 Lectura activada con gatillo")
    }

    fun disableTrigger() {
        isReading = false
        stopInventory()
        onTagReadCallback = null // Limpiar callback
        Log.d(TAG, "⛔ Lectura desactivada")
    }


    private var onTagReadCallback: ((String) -> Unit)? = null
}
