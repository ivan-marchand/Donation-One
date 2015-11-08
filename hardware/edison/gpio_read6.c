/*
 * Author: Thomas Ingleby <thomas.c.ingleby@intel.com>
 * Copyright (c) 2014 Intel Corporation.
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

#include "stdio.h"
#include "unistd.h"

#include "mraa.h"

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>

#include <netinet/tcp.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <netdb.h>


int socket_connect(char *host, in_port_t port){
	struct hostent *hp;
	struct sockaddr_in addr;
	int on = 1, sock;     

	if((hp = gethostbyname(host)) == NULL){
		herror("gethostbyname");
		exit(1);
	}
	bcopy(hp->h_addr, &addr.sin_addr, hp->h_length);
	addr.sin_port = htons(port);
	addr.sin_family = AF_INET;
	sock = socket(PF_INET, SOCK_STREAM, IPPROTO_TCP);
	setsockopt(sock, IPPROTO_TCP, TCP_NODELAY, (const char *)&on, sizeof(int));

	if(sock == -1){
		perror("setsockopt");
		exit(1);
	}
	
	if(connect(sock, (struct sockaddr *)&addr, sizeof(struct sockaddr_in)) == -1){
		perror("connect");
		exit(1);

	}
	return sock;
}

#define BUFFER_SIZE 1024
int fd;
char buffer[BUFFER_SIZE];

void sendMoney(int amount) {

        char* IP="45.33.81.168";

        fd = socket_connect(IP, 10110);

        char* query = "GET /ws/api/v1/oneClickPayment?email=juju@juju.juju&amount=1\r\n\r\n";
        if (amount==1) {
	   query = "GET /ws/api/v1/oneClickPayment?email=juju@juju.juju&amount=2\r\n\r\n";	
        }
 
	write(fd, query, strlen(query)); // write(fd, char[]*, len);  
	bzero(buffer, BUFFER_SIZE);
	
	while(read(fd, buffer, BUFFER_SIZE - 1) != 0){
		fprintf(stderr, "%s", buffer);
		bzero(buffer, BUFFER_SIZE);
	}

	shutdown(fd, SHUT_RDWR); 
	close(fd); 
}


int
main(int argc, char **argv)
{
    mraa_init();
    fprintf(stdout, "MRAA Version: %s\nStarting Read on IO6\n",
            mraa_get_version());

//! [Interesting]
    mraa_gpio_context gpio;
    mraa_gpio_context gpio2;

    gpio = mraa_gpio_init(6);
    mraa_gpio_dir(gpio, MRAA_GPIO_IN);

    gpio2 = mraa_gpio_init(2);
    mraa_gpio_dir(gpio2, MRAA_GPIO_IN);

    for (;;) {
        
        fprintf(stdout, "Gpio is %d\n", mraa_gpio_read(gpio));
        if (mraa_gpio_read(gpio)==1){
	   sendMoney(0);   	
	}

        fprintf(stdout, "Gpio2 is %d\n", mraa_gpio_read(gpio2));
	if (mraa_gpio_read(gpio2)==1){
           sendMoney(1);
        }

        sleep(1);
    }

    mraa_gpio_close(gpio);
    mraa_gpio_close(gpio2);
//! [Interesting]

    return 0;
}
