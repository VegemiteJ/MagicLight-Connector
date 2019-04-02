/**
 *  MagicLight Connector
 *
 *  Copyright 2019 Jack Baxter
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 */
metadata {
	definition (name: "MagicLight Connector", namespace: "VegemiteJ", author: "Jack Baxter", cstHandler: true) {
		capability "Switch"
	}


	simulator {
		// TODO: define status and reply messages here
	}

	tiles {
		// TODO: define your main and details tiles here
	}
}

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"
	// TODO: handle 'switch' attribute

}

// handle commands
def on() {
	log.debug "Executing 'on' for $device.deviceNetworkId"
    log.debug device.properties
    .sort{it.key}
    .collect{it}
    .findAll{!['class', 'active'].contains(it.key)}
    .join('\n')
	try {
        def result = new physicalgraph.device.HubAction(
            method: "POST",
            path: "/MagicLight",
            headers: [
                HOST: "192.168.0.176:8000"
            ],
            query: [LightName: "$device.deviceNetworkId", State: "on"]
        )
        sendHubCommand(result)
    } catch (e) {
        log.error "something went wrong: $e"
    }
}

def off() {
	log.debug "Executing 'off' for $device.deviceNetworkId"
    log.debug device.properties
    .sort{it.key}
    .collect{it}
    .findAll{!['class', 'active'].contains(it.key)}
    .join('\n')
    try {
        def result = new physicalgraph.device.HubAction(
            method: "POST",
            path: "/MagicLight",
            headers: [
                HOST: "192.168.0.176:8000"
            ],
            query: [LightName: "$device.deviceNetworkId", State: "off"]
        )
        sendHubCommand(result)
    } catch (e) {
        log.error "something went wrong: $e"
    }
}