import java.math.BigDecimal
import java.math.RoundingMode

//Задача №3 - "Меломан"


class User(var id: Long?, var meloman: Boolean, var previousSum: BigDecimal)
class NewPurchase(var user: User, var sum: BigDecimal)
class Transfer(var user: User, var totalPrice: BigDecimal)

fun main() {
    //Tests

    run {
        //User 1  Покупал до тысячи, не меломан
        val user = User(1, false, BigDecimal(999.00))
        displayResult(computeDiscont(NewPurchase(user, BigDecimal(1000.00))))
    }

    run {
        //User 2  Покупал на тысячу ровно, не меломан, берет на маленькую сумму
        val user = User(2, false, BigDecimal(1000))
        displayResult(computeDiscont(NewPurchase(user, BigDecimal(1000.00))))
    }

    run {  //User 3  Покупал на 1001 ровно, не меломан
        val user = User(3, false, BigDecimal(1001))
        displayResult(computeDiscont(NewPurchase(user, BigDecimal(1000.00))))

    }

    run {  //User 4  Покупал на 10_000 ровно, не меломан
        val user = User(4, false, BigDecimal(10_000))
        displayResult(computeDiscont(NewPurchase(user, BigDecimal(1000.00))))
    }

    run {  //User 5  Покупал на 10_001 , не меломан
        val user = User(5, false, BigDecimal(10_001))
        displayResult(computeDiscont(NewPurchase(user, BigDecimal(100.00))))
    }

    run {  //User 6  Покупал на 10_001 , меломан
        val user = User(6, true, BigDecimal(10_001))
        displayResult(computeDiscont(NewPurchase(user, BigDecimal(100.00))))
    }
}

//Вывод результата
fun displayResult(transfer: Transfer) {
    println(
        " id= ${transfer.user.id} к оплате=" +
                " ${
                    transfer.totalPrice
                        .setScale(
                            2,
                            RoundingMode.CEILING
                        )
                } рублей"
    )
}


// сумма покупок от 10 001 и выше то % составляет 5% от суммы
fun calc5percentDicont(value: BigDecimal): BigDecimal {
    val result: BigDecimal = value.multiply(BigDecimal.valueOf(0.05))
    return value.minus(result)
}

// Меломан? Да есть скидка один процент
fun calc1percentDicont(value: BigDecimal): BigDecimal {
    val result: BigDecimal = value.multiply(BigDecimal.valueOf(0.01))
    return value.minus(result)
}

// сумма покупок от 1 001 до 10 000. Скидка 100 рублей
fun calc100rubDiscont(value: BigDecimal): BigDecimal {
    if (value <= BigDecimal(100.00)) return value
    return value.minus(BigDecimal(100.00))
}

//Проверка на меломана для 1001 случая
fun makeDiscont1001(user: User, amount: BigDecimal): BigDecimal {
    return if (user.meloman) {
        calc1percentDicont(calc100rubDiscont(amount))
    } else calc100rubDiscont(amount)
}

//Проверка на меломана для 10_001 случая
fun makeDiscont10_001(user: User, amount: BigDecimal): BigDecimal {
    return if (user.meloman) {
        calc1percentDicont(calc5percentDicont(amount))
    } else calc5percentDicont(amount)
}


fun computeDiscont(newPurchase: NewPurchase): Transfer {
    return when (newPurchase.user.previousSum.toInt()) {
        0 -> Transfer(newPurchase.user, newPurchase.sum) //Error
        in 1..1000 -> Transfer(newPurchase.user, newPurchase.sum) //без скидки
        in 1001..10_000 -> Transfer(newPurchase.user, makeDiscont1001(newPurchase.user, newPurchase.sum))
        /*in 10_001.. Int.MAX_VALUE */
        else -> Transfer(newPurchase.user, makeDiscont10_001(newPurchase.user, newPurchase.sum))
    }

}


