import java.math.RoundingMode

// ---------------------------
//
// HOMEWORK
//
// Use Groovy to write a code under "YOUR CODE GOES BELOW THIS LINE" comment.
// Make sure the code is working in some of the web Groovy consoles, e.g. https://groovyconsole.appspot.com
// Do not over-engineer the solution.
//
// Assume you got some data from a customer and your task is to design a routine that will calculate the average Product price per Group.
//
// The Price of each Product is calculated as:
// Cost * (1 + Margin)
//
// Assume there can be a large number of products.
//
// Plus points:
// - use Groovy closures (wherever it makes sense)
// - make the category look-up performance effective

// contains information about [Product, Group, Cost]
def products = [
        ["A", "G1", 20.1],
        ["B", "G2", 98.4],
        ["C", "G1", 49.7],
        ["D", "G3", 35.8],
        ["E", "G3", 105.5],
        ["F", "G1", 55.2],
        ["G", "G1", 12.7],
        ["H", "G3", 88.6],
        ["I", "G1", 5.2],
        ["J", "G2", 72.4]
]

// contains information about Category classification based on product Cost
// [Category, Cost range from (inclusive), Cost range to (exclusive)]
// i.e. if a Product has Cost between 0 and 25, it belongs to category C1
// ranges are mutually exclusive and the last range has a null as upper limit.
def category = [
        ["C3", 50, 75],
        ["C4", 75, 100],
        ["C2", 25, 50],
        ["C5", 100, null],
        ["C1", 0, 25]
]

// contains information about margins for each product Category
// [Category, Margin (either percentage or absolute value)]
def margins = [
        "C1": "20%",
        "C2": "30%",
        "C3": "0.4",
        "C4": "50%",
        "C5": "0.6"
]

// ---------------------------
//
// YOUR CODE GOES BELOW THIS LINE
//
// Assign the 'result' variable so the assertion at the end validates
//
// ---------------------------
static def findCategory(def weight, def category) {
    for (el in category) {
        if (weight >= el.get(1) && (weight < el.get(2) || el.get(2) == null)) {
            return el.get(0)
        }
    }
    throw new RuntimeException("Not found")
}

static def average(def list) {
    return list.sum() / list.size()
}

def formattedMargins = [:]
for (k in margins) {
    if (k.value.contains("%")) {
        k.value = "0." + k.value.replace("%", "")
    }
    formattedMargins.put(k.key, Double.valueOf(k.value))
}

def map = products.groupBy { p -> p.get(1) }
println(map)

def newMap = [:]
for (k in map) {
    def newList = []
    for (list in k.value) {
        newList.add(list.get(2))
    }
    newMap.put(k.key, newList)
}
println(newMap)

def finalMap = [:]

for (k in newMap) {
    def newList = []
    for (el in k.value) {
        def index = findCategory(el as Double, category as ArrayList<List<Serializable>>)
        def margin = formattedMargins.get(index)
        def newEl = el * (1 + margin)
        newList.add(newEl)
    }
    finalMap.put(k.key, newList)
}
println(finalMap)

def result = finalMap.collectEntries { key, value -> [(key): new BigDecimal(average(value as List<Double>))
        .setScale(1, RoundingMode.HALF_UP)] }
println(result)

// ---------------------------
//
// IF YOUR CODE WORKS, YOU SHOULD GET "It works!" WRITTEN IN THE CONSOLE
//
// ---------------------------
assert result == [
        "G1": 37.5,
        "G2": 124.5,
        "G3": 116.1
]: "It doesn't work"

println "It works!"