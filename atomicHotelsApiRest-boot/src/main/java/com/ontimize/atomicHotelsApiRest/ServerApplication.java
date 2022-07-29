package com.ontimize.atomicHotelsApiRest;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.ontimize.atomicHotelsApiRest.api.core.service.IHotelService;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelDao;
import com.ontimize.atomicHotelsApiRest.model.core.service.HotelService;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@SpringBootApplication
public class ServerApplication {

//	private IHotelService hotelService;

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
		System.err.println("        /@&   @@.                                   \r\n"
				+ "                                   @(       %@                                  \r\n"
				+ "                                 (@           @(                                \r\n"
				+ "                                %@             @&                               \r\n"
				+ "                               *@               @(                              \r\n"
				+ "                               @.                @                              \r\n"
				+ "                              @@                 @@                             \r\n"
				+ "     (@@@@@@@%/.             .@                   @    .@%#((#      .,,/*,      \r\n"
				+ "  @@              *@@@@(     @&                   &@  /@&#/,,/#            ,@@  \r\n"
				+ " ,@                      .@@@@                     @@@ @@&%%%%&               @ \r\n"
				+ "  @(                        (@  %@@%         /@@@, @&    *%%,                @@ \r\n"
				+ "   @@                       @@       *@@@@@@       *@                       @@  \r\n"
				+ "    .@(                     @(      %@@*  #@@,      @                     %@    \r\n"
				+ "      *@, (%##              @. *@@&   ((//,   #@@,  @.                  %@,     \r\n"
				+ "        @@#(,.*(,          #@@. %%((/(**..,*/(((. %@@,                @@        \r\n"
				+ "        @@&#((#%%      #@@. @ .&%(*,.*(%#(/*(*..*(  @.#@@          &@/          \r\n"
				+ "         /@@@@@     @@,    .@  &&%#(#&#(*.,*(/((#%  @.    @@%   @@(             \r\n"
				+ "                ,@@&       (@   #(/*%&%#(/////**/   @.       @@@                \r\n"
				+ "              @@    ,@@.   (@ .%(/*..*&&&&&#/*..,/. @    .@@.   .@@             \r\n"
				+ "           @@,          &@@#@  %%#(/%&#(*,%%#(//(#  @ %@@          #@#          \r\n"
				+ "        ,@&                *@@@,.%%%%&&%#((#%%%*  #@@.                @@        \r\n"
				+ "      %@                   .@   #@@.  /&&&&   @@@  .@                   @@.     \r\n"
				+ "    (@.                     @.      /@@( %@@*      (@                     &@    \r\n"
				+ "   @&                       @@      /@@# @@@       @&                       @&  \r\n"
				+ "  @#                        (@ %@@#          ,@@@/ @/                        @@ \r\n"
				+ " (@                     .&@@@@.                   ,@@@@@#                    #@ \r\n"
				+ "  %@/          .#@@@@&       @&                  **        (@@@@@/.       .&@@  \r\n"
				+ "       .,,..                  @               @&#/*,/(                          \r\n"
				+ "                              @%              @@%#(/(#/                         \r\n"
				+ "                              .@               @@@@@@.                          \r\n"
				+ "                               #@               @,                              \r\n"
				+ "                                @@             @(                               \r\n"
				+ "                                 #@           @,                                \r\n"
				+ "                                   @#       @@                                  \r\n"
				+ "                                    ,@@##@@&                                    \r\n"
				+ "");
		System.out.println("********************** ¡Vámonos Átomos! ************************************");
		System.err.println("****************************************************************************");
//		IHotelService hotelService = new HotelService();
//		HotelDao hotelDao =  new HotelDao();
//		DefaultOntimizeDaoHelper daoHelper = new DefaultOntimizeDaoHelper();
//		EntityResult resultado =  daoHelper.query(hotelDao,new HashMap<String, Object>(),
//				new ArrayList<String>() {{ add(HotelDao.ATTR_NAME);}});
		
	}

}
