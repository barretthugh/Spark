// def checkEven(num:Int): Boolean = {
//   return num%2 == 0
// }

def checkEven(num:Int)= num%2 == 0

// println(checkEven(4))
// println(checkEven(3))


def checkList(numbers: List[Int]): Boolean = {
  for (n <- numbers){
    if (n%2==0){
      return true
    }
  }
  return false
}

// val evensample = List(1,2,3,4,5)
// val oddsample = List(1,3,5,7)
//
// println(checkList(evensample))
// println(checkList(oddsample))

def lucky(nums: List[Int]): Int = {
  var output = 0
  for (n <- nums){
    if (n == 7){
      output = output + 14
    }else{
      output = output + n
    }
  }
  return output
}

// val numbers = List(1,2,3,7)
// println(lucky(numbers))


def balanceCheck(mylist: List[Int]): Boolean = {
  var firsthalf = 0
  var secondhalf = 0

  secondhalf = mylist.sum

  for (i <- Range(0,mylist.length)){
    firsthalf = firsthalf + mylist(i)
    secondhalf = secondhalf - mylist(i)

    if (firsthalf == secondhalf){
      return true
    }
  }
  return false
}

// val ballist = List(1,2,3,4,10)
// val ballist2 = List(2,3,3,2)
// val unballist = List(10,20,70)
//
// println(balanceCheck(ballist))
// println(balanceCheck(ballist2))
// println(balanceCheck(unballist))


def palindromeCheck(st:String): Boolean = {
  return (st == st.reverse)
}

println(palindromeCheck("abccba"))
println(palindromeCheck("hello"))
