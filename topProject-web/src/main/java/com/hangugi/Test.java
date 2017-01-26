package com.hangugi;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class Test implements ServletContextListener {

	public void contextDestroyed(ServletContextEvent arg0) {
		System.out.println("stopppppppppppppppppppppppppppppppppppppppppppp");
		
	}

	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("starttttttttttttttttttttttttttttttttttttttttttt");
	}
}
