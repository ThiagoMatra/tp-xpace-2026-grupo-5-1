import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import java.time.LocalDate

class SubvencionTest : DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest

    describe("Subvencion") {
        val planetaBase = Planeta().apply {
            distanciaTierra = 10.0F
            fueAterrizado = true
            temperaturaMedia = 20f
            gravedad = 10f
            hayAguaLiquida = true
            toxicidadAtmosfera = 10f
            nivelRadiacion = 10f
        }

        val naveBase = Carguero().apply {
            velocidadPromedio = 100.0F
            consumoBase = 40.0
            fechaFabricacion = LocalDate.now()
        }

        val misionBase = Mision().apply {
            planetaDestino = planetaBase
            naveAsignada = naveBase
        }


        it("Subvencion con mas presupuesto que mision") {
                misionBase.naveAsignada.consumoBase = 1.0

                val subvencionCara = Subvencion().apply {
                    montoBase = 1000f
                }
                subvencionCara.montoAplicable(misionBase) shouldBe false


        }
        it("Subvencion de Emergencia con monto de 15000") {
            val subvencionEmergencia = Subvencion().apply {
            tipo = DeEmergencia()
            montoBase = 5000F
            fechaEmision = LocalDate.now()
            duracion = 50
        }
            subvencionEmergencia.montoSubvencion(misionBase) shouldBe 15000F
            subvencionEmergencia.esAplicable(misionBase) shouldBe true
        }
        it("Subvencion ya aplicada no es aplicable nuevamente ") {

            val subvencionAplicada = Subvencion().apply {
                montoBase = 10000F
                fechaEmision = LocalDate.now().minusMonths(1)
                duracion = 5
                aplicada = true
            }
            subvencionAplicada.esAplicable(misionBase) shouldBe false
        }

        it("Subvencion Vencida") {

            val subvencionVencida = Subvencion().apply {
                montoBase = 10000F
                fechaEmision = LocalDate.now().minusMonths(2)
                duracion = 1
            }
            subvencionVencida.vencida() shouldBe true
            subvencionVencida.esAplicable(misionBase) shouldBe false
        }

        it("Subvencion Gubernamental con planeta habitable") {
            val planetaInexplorado = Planeta().apply {
                fueAterrizado = false
                temperaturaMedia = 20f
                gravedad = 10f
                hayAguaLiquida = true
            }
            misionBase.planetaDestino = planetaInexplorado

            val subvencionGubernamental = Subvencion().apply {
                tipo = Gubernamental()
                montoBase = 10000F
                fechaEmision = LocalDate.now().minusMonths(1)
                duracion = 70
            }
            subvencionGubernamental.esAplicable(misionBase) shouldBe true
            subvencionGubernamental.montoSubvencion(misionBase) shouldBe 13000F
        }
        it("Subvencion Gubernamental con planeta no  habitable") {
            val planetaInexplorado = Planeta().apply {
                fueAterrizado = false
                temperaturaMedia = 20f
                gravedad = 35f
                hayAguaLiquida = true
            }
            misionBase.planetaDestino = planetaInexplorado

            val subvencionGubernamental = Subvencion().apply {
                tipo = Gubernamental()
                montoBase = 10000F
                fechaEmision = LocalDate.now().minusMonths(1)
                duracion = 50
            }
            subvencionGubernamental.esAplicable(misionBase) shouldBe true
            subvencionGubernamental.montoSubvencion(misionBase) shouldBe 11500F
        }

        it("Subvencion Privada con experiencia promedio mayor a 5") {

            val tripulanteExpermientado = Tripulante().apply {
                fechaInicio = LocalDate.now().minusYears(15) // > 10 años
                misionesExitosas = 50 // Experiencia > 5
                salarioBase = 5000f
                fechaNacimiento = LocalDate.of(1980, 5, 5)
            }
            val GuiaExpermientado = Tripulante().apply {
                fechaInicio = LocalDate.now().minusYears(15)
                misionesExitosas = 50
                salarioBase = 5000f
                fechaNacimiento = LocalDate.of(1980, 5, 5)
            }
            //misionBase.tripulantes.clear()
            misionBase.registrarTripulante(GuiaExpermientado)
            misionBase.registrarTripulante(tripulanteExpermientado)

            val subvencionPrivadaAceptable = Subvencion().apply {
                tipo = Privada()
                montoBase = 10000F
                fechaEmision = LocalDate.now().minusMonths(1)
                duracion = 50

            }
            subvencionPrivadaAceptable.esAplicable(misionBase) shouldBe true
            subvencionPrivadaAceptable.montoSubvencion(misionBase) shouldBe 30000F

        }

        it("subvencion privada con experiencia promedio menor a 5 no acepta") {
            val planeta = Planeta().apply {
                fueAterrizado = false
                temperaturaMedia = 2.2F
                gravedad = 20.2F
                hayAguaLiquida = true
                toxicidadAtmosfera = 20.0F
            }

            val tripulanteExpermientado = Tripulante().apply {
                misionesExitosas = 10
                fechaInicio = LocalDate.now().minusMonths(4)
            }
            val guiaExperimentado = Tripulante().apply {
                fechaInicio = LocalDate.now().minusMonths(4)
                misionesExitosas = 10
            }
            val navenueva = Carguero().apply {
                consumoBase = 40.0
                fechaFabricacion = LocalDate.now()
                velocidadPromedio = 100.0F
            }
            val mision = Mision().apply {
                planetaDestino = planeta
                naveAsignada = navenueva
                //tripulantes.add(tripulanteExpermientado)
            }
            mision.registrarTripulante(guiaExperimentado)
            mision.registrarTripulante(tripulanteExpermientado)

            val subvencionPrivadaAceptable = Subvencion().apply {
                tipo = Privada()
                montoBase = 10000F
                fechaEmision = LocalDate.now().minusMonths(1)
                duracion = 50
            }
            subvencionPrivadaAceptable.esAplicable(misionBase) shouldBe false


        }

    }
})