import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import java.time.LocalDate

class NaveTests : DescribeSpec({

    describe("Testeando combustibles") {
        val planeta = Planeta().apply{
            distanciaTierra = 10F
        }

        it("Consumo Sonda"){
            val sonda50 = Sonda().apply{
                consumoBase = 50.0
        }
            sonda50.consumoNave() shouldBe 50.0
            sonda50.consumoTotal(planeta) shouldBe 500 // 50 * 10
            sonda50.consumoNave() shouldNotBe 35.0
        }

        it("Consumo Tansbordador"){
            val transbordador5 = Transobrador().apply{
                consumoBase = 50.0
                //repeat(5){ tripulantes.add(Tripulante())}
            }
            transbordador5.consumoNave(5) shouldBe 75.0 // 50 + (50*0.5)
            transbordador5.consumoTotal(planeta, 5) shouldBe 750 // 75 * 10
            transbordador5.consumoNave(5) shouldNotBe 50.0
        }
        it("Consumo Carguero moderno"){
            val carguero25 = Carguero().apply{
                consumoBase = 50.0
                carga = 25
                fechaFabricacion = LocalDate.now().minusDays(2)
            }

            carguero25.consumoNave() shouldBe 112.5 // 50 + 50 * (0.05 * 25)
            carguero25.consumoTotal(planeta, ) shouldBe 1125.0 // 112.5 * 10
            carguero25.consumoNave() shouldNotBe 135
        }

        it("Consumo Carguero viejo"){
            val carguero50 = Carguero().apply{
                consumoBase = 50.0
                carga = 50
                fechaFabricacion = LocalDate.now().minusYears(35)
            }
            carguero50.consumoNave() shouldBe 210 // (50 + 50 * (0,05 * 50)) * 1.2 (es viejo + 20%)
            carguero50.consumoTotal(planeta) shouldBe 2100
            carguero50.consumoNave() shouldNotBe 175
        }
    }

    describe("Puede alcanzar planeta"){
        val planeta = Planeta().apply{
            distanciaTierra = 5F
        }
        it("Puede alcanzarlo"){
            val sondaVeloz = Sonda().apply{
                velocidadPromedio = 100F
                autonomia = 60 // dias
            }

            sondaVeloz.tiempoDeViaje(planeta) shouldBe 36.5 // 5 * 365 / 100 * 2
            sondaVeloz.puedeAlcanzar(planeta) shouldBe true // 36.5 < 60
    }
        it("No puede alcanzarlo"){
            val sondaLenta = Sonda().apply{
                velocidadPromedio = 25F
                autonomia = 30
            }

            sondaLenta.tiempoDeViaje(planeta) shouldBe 146 // 5 * 365 / 25 * 2
            sondaLenta.puedeAlcanzar(planeta) shouldBe false // 146 > 30

        }

    }

//    describe("Naves Aptas para Mision"){
//        it ("Sondas tripuladas y no tripuladas"){
//            val sondaTripulada = Sonda().apply{
//                //tripulantes.add(Tripulante())
//            }
//            val sondaVacia = Sonda()
//
//            sondaTripulada.cantidadTripulantesOk(1) shouldBe false
//            sondaVacia.cantidadTripulantesOk() shouldBe true
//        }
//        it ("Transbordadores vs capacidadNave"){
//            val transbordadorChico = Transobrador().apply{
//                capacidadTripulantes = 2
//                //repeat (5) {tripulantes.add(Tripulante())}
//            }
//            val transbordadorGrande = Transobrador().apply{
//                capacidadTripulantes = 10
//                //repeat (5) {tripulantes.add(Tripulante())}
//            }
//
//            transbordadorChico.cantidadTripulantesOk(5) shouldBe false
//            transbordadorGrande.cantidadTripulantesOk(5) shouldBe true
//
//            }
//        }

    describe("Naves validas y no validas"){
        it ("Es valida si todos los datos estan"){
            val naveApta = Sonda().apply{
                nombre = "NaveApta"
                codigoID = "AP-01"
                velocidadPromedio = 100F
                autonomia = 60
                consumoBase = 50.0
            }
            naveApta.esValido() shouldBe true
        }
        it ("No es valida si faltan datos"){
            val naveNoApta = Sonda().apply{}
            naveNoApta.esValido() shouldBe false
            naveNoApta.nombreValido() shouldBe false
            naveNoApta.codigoValido() shouldBe false
            naveNoApta.velocidadValido() shouldBe false
            naveNoApta.autonomiaValido() shouldBe false
            naveNoApta.consumoValido() shouldBe false
        }
    }

})