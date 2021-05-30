import groovy.json.JsonSlurper

//this will be your URL which will return something, tweak it if you want to pass parameters or username/password acc.

def SOME_URL = "https://ifconfig.me/all.json"

// now connect to the URL and create a connection variable 'conn'
def conn = SOME_URL.toURL().openConnection()

// create a list variable 'servernames'
def servernames = []

// if connection response was successful i.e. http protocol return code was 200, then do the following
if( conn.responseCode == 200 ) {
    // get the results / output of the URL connection in a variable 'results'
    def results = new JsonSlurper().parseText(conn.content.text)

    // to see results variable output uncomment the next line
    //println results

    // now read each element in the 'results' variable and pick servername/somefield variable into the list variable 'servernames'
    results.each { id, data -> servernames.push(data) }
}
return servernames.sort().unique()
// return servernames.sort()
