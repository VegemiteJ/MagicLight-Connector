import os
from http.server import HTTPServer, BaseHTTPRequestHandler
from urllib.parse import urlparse, parse_qs
from config import mapping as config_mapping

class HttpStartup:
    def __init__(self):
        SimpleHTTPRequestHandler.DeviceNetworkMapping = config_mapping
        httpd = HTTPServer(('', 8000), SimpleHTTPRequestHandler)
        httpd.serve_forever()

class SimpleHTTPRequestHandler(BaseHTTPRequestHandler):

    DeviceNetworkMapping = None

    def do_GET(self):
        self.send_response(200)
        self.end_headers()
        self.wfile.write(b'Hello, world!')
        self.print_request_details()

    def do_POST(self):
        try:
            self.print_request_details()
            
            # Parse path and variables
            path, tmp = self.path.split('?',1)
            results = parse_qs(tmp)
            deviceName = results['LightName'][0]
            deviceNetworkPath = self.DeviceNetworkMapping[deviceName]
            deviceState = self.parse_device_state(results['State'][0])
            
            # Send request
            cmd = f"flux_led {deviceNetworkPath} --{deviceState}"
            print(f"Changing device state with command: `{cmd}`")
            os.system(cmd)
            self.send_response(200)
            
        except Exception as e:
            print("Failed")
            print(e)

    def parse_device_state(self, state):
        if len(state) < 2 or len(state) > 3:
            raise Exception("Invalid State")
        if "off" in state:
            return "off"
        if "on" in state:
            return "on"
        raise Exception("Invalid State")

    def print_request_details(self):
        print(f"Request from: {self.request.getpeername()}\n")
        print(f"Headers: {self.headers}\n")
        print(f"Content: {self.raw_requestline}")

if __name__ == "__main__":
    server = HttpStartup()
