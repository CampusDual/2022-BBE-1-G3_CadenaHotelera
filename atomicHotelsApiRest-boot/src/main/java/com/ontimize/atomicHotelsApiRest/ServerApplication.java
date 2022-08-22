package com.ontimize.atomicHotelsApiRest;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.ontimize.atomicHotelsApiRest.api.core.service.IHotelService;
import com.ontimize.atomicHotelsApiRest.model.core.dao.HotelDao;
import com.ontimize.atomicHotelsApiRest.model.core.service.HotelService;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = false)
public class ServerApplication {
    private static ConfigurableApplicationContext context;

//	private IHotelService hotelService;

	public static void main(String[] args) {
		context = SpringApplication.run(ServerApplication.class, args);
		logoPrint();
	}
	
	/**
	 * https://www.baeldung.com/java-restart-spring-boot-app
	 */
	public static void restart() {
        ApplicationArguments args = (ApplicationArguments) context.getBean(SpringApplication.class);

        Thread thread = new Thread(() -> {
            context.close();
            context = SpringApplication.run(SpringApplication.class, args.getSourceArgs());
        });

        thread.setDaemon(false);
        thread.start();
    }
	
	
	public static void logoPrint() {
		try {
		//   System.err.println("                                    /@&   @@.                                   \r\n"
//							+ "                                   @(       %@                                  \r\n"
//							+ "                                 (@           @(                                \r\n"
//							+ "                                %@             @&                               \r\n"
//							+ "                               *@               @(                              \r\n"
//							+ "                               @.                @                              \r\n"
//							+ "                              @@                 @@                             \r\n"
//							+ "     (@@@@@@@%/.             .@                   @    .@%#((#      .,,/*,      \r\n"
//							+ "  @@              *@@@@(     @&                   &@  /@&#/,,/#            ,@@  \r\n"
//							+ " ,@                      .@@@@                     @@@ @@&%%%%&               @ \r\n"
//							+ "  @(                        (@  %@@%         /@@@, @&    *%%,                @@ \r\n"
//							+ "   @@                       @@       *@@@@@@       *@                       @@  \r\n"
//							+ "    .@(                     @(      %@@*  #@@,      @                     %@    \r\n"
//							+ "      *@, (%##              @. *@@&   ((//,   #@@,  @.                  %@,     \r\n"
//							+ "        @@#(,.*(,          #@@. %%((/(**..,*/(((. %@@,                @@        \r\n"
//							+ "        @@&#((#%%      #@@. @ .&%(*,.*(%#(/*(*..*(  @.#@@          &@/          \r\n"
//							+ "         /@@@@@     @@,    .@  &&%#(#&#(*.,*(/((#%  @.    @@%   @@(             \r\n"
//							+ "                ,@@&       (@   #(/*%&%#(/////**/   @.       @@@                \r\n"
//							+ "              @@    ,@@.   (@ .%(/*..*&&&&&#/*..,/. @    .@@.   .@@             \r\n"
//							+ "           @@,          &@@#@  %%#(/%&#(*,%%#(//(#  @ %@@          #@#          \r\n"
//							+ "        ,@&                *@@@,.%%%%&&%#((#%%%*  #@@.                @@        \r\n"
//							+ "      %@                   .@   #@@.  /&&&&   @@@  .@                   @@.     \r\n"
//							+ "    (@.                     @.      /@@( %@@*      (@                     &@    \r\n"
//							+ "   @&                       @@      /@@# @@@       @&                       @&  \r\n"
//							+ "  @#                        (@ %@@#          ,@@@/ @/                        @@ \r\n"
//							+ " (@                     .&@@@@.                   ,@@@@@#                    #@ \r\n"
//							+ "  %@/          .#@@@@&       @&                  **        (@@@@@/.       .&@@  \r\n"
//							+ "       .,,..                  @               @&#/*,/(                          \r\n"
//							+ "                              @%              @@%#(/(#/                         \r\n"
//							+ "                              .@               @@@@@@.                          \r\n"
//							+ "                               #@               @,                              \r\n"
//							+ "                                @@             @(                               \r\n"
//							+ "                                 #@           @,                                \r\n"
//							+ "                                   @#       @@                                  \r\n"
//							+ "                                    ,@@##@@&                                    \r\n" 
//		   					+"\r\n"
//							+"********************************************************************************");
//			Thread.sleep(20);
//			System.out.println("***************************** ¡Vámonos Átomos! *********************************");
//			Thread.sleep(20);
//			System.err.println("********************************************************************************");

//		   System.err.println("                                                                                                                                              \r\n"
//					   		+ "                                          ▓▓    ██████▒▒                                                            \r\n"
//					   		+ "                                        ▓▓    ██████▓▓██▓▓                                                          \r\n"
//					   		+ "                                        ▓▓  ░░████▓▓██████                                                          \r\n"
//					   		+ "                                      ▒▒      ██▓▓██████▓▓                                                          \r\n"
//					   		+ "                      ▓▓████▓▓░░      ██        ██████▓▓      ▓▓████▒▒                                              \r\n"
//					   		+ "                    ██░░    ░░▓▓██▓▓                      ▓▓▓▓▒▒    ▓▓▒▒                                            \r\n"
//					   		+ "                    ██            ████                ░░▓▓▒▒        ░░██                                            \r\n"
//					   		+ "                    ██              ▒▒██            ▓▓▓▓░░            ██                                            \r\n"
//					   		+ "                    ▓▓              ▓▓  ▓▓▒▒    ██▓▓    ░░            ▓▓                                            \r\n"
//					   		+ "                    ▓▓              ██      ▒▒██▒▒    ░░▒▒          ▓▓▒▒                                            \r\n"
//					   		+ "                    ░░▓▓            ██    ▓▓▓▓          ██          ██                                              \r\n"
//					   		+ "                      ██            ██  ▒▒██    ▓▓▒▒    ██        ▒▒▓▓                                              \r\n"
//					   		+ "                      ▓▓▓▓  ▓▓██▒▒  ░░░░██        ██▒▒  ░░  ██▓▓  ██                                                \r\n"
//					   		+ "                  ▒▒██  ▓▓▒▒          ██    ░░░░    ▓▓██        ▒▒▒▒  ██░░                                          \r\n"
//					   		+ "                ██▓▓░░    ▓▓        ▓▓  ░░▓▓██████░░  ▓▓▓▓      ██    ▒▒██▒▒                                        \r\n"
//					   		+ "              ██░░        ▓▓▓▓    ▓▓▓▓  ████████████    ▓▓░░  ▓▓▓▓        ▓▓██                                      \r\n"
//					   		+ "            ██▒▒            ▓▓  ░░██    ████████████▓▓    ▒▒  ██                                                    \r\n"
//					   		+ "            ▓▓                ░░██    ▒▒██████████████      ██░░        ████▓▓                                      \r\n"
//					   		+ "            ▓▓              ░░██░░    ▒▒██████████████    ▒▒▒▒        ████████▓▓                                    \r\n"
//					   		+ "            ██▒▒            ██░░  ▓▓    ██████████▓▓▓▓    ██  ▓▓      ██████████                                    \r\n"
//					   		+ "              ▓▓▒▒        ▓▓▒▒    ▒▒██  ████████▓▓██    ██    ▒▒▓▓    ██████████                                    \r\n"
//					   		+ "                ████▓▓  ░░██        ▓▓▒▒  ████▓▓██    ██▓▓      ██    ▓▓██████▒▒                                    \r\n"
//					   		+ "                  ▒▒██  ▓▓▒▒          ██    ░░      ▓▓██        ▒▒▒▒    ████▓▓                                      \r\n"
//					   		+ "                      ░░▓▓  ▓▓██▒▒    ▓▓▓▓        ▓▓▓▓      ▓▓▓▓  ██                                                \r\n"
//					   		+ "                      ▓▓░░          ██  ▓▓▒▒    ▓▓▓▓    ██        ▓▓▒▒                                              \r\n"
//					   		+ "                    ░░▒▒            ██      ▓▓██░░      ▓▓          ██                                              \r\n"
//					   		+ "                    ▓▓              ██    ▓▓██        ░░██          ▓▓▒▒                                            \r\n"
//					   		+ "                    ██              ░░  ▓▓▒▒    ▓▓▓▓    ▓▓            ▓▓                                            \r\n"
//					   		+ "                    ██                ██▓▓        ▒▒██░░              ██                                            \r\n"
//					   		+ "                    ░░  ██████▓▓    ▓▓▓▓            ▒▒██▓▓            ██                                            \r\n"
//					   		+ "                      ████████▓▓▒▒  ▒▒                  ▒▒██▒▒      ▒▒▓▓                                            \r\n"
//					   		+ "                      ██████▓▓████    ██            ▓▓▒▒    ▓▓████████                                              \r\n"
//					   		+ "                      ████▓▓████▓▓    ▓▓            ██                                                              \r\n"
//					   		+ "                        ▓▓████▓▓        ██        ▓▓▒▒                                                              \r\n"
//					   		+ "                                        ▓▓      ▒▒▓▓                                                                \r\n"
//					   		+ "                                          ██▓▓████                                                                  \r\n"
//					   		+ "                                              ░░                                                                    \r\n"
//					   		+ "");
//		  
//			Thread.sleep(20);
//			System.err.println("     ********************************************************************************");
//			Thread.sleep(20);
//			System.out.println("     ****************************  ¡Vámonos Átomos!  ********************************");
//			Thread.sleep(20);
//			System.err.println("     ********************************************************************************");

		System.err.println(""
		   		+ "                         ^?YY~                                                                                                                        \r\n"
		   		+ "                  .~?J~ :GBBB~                                                                                                                        \r\n"
		   		+ "                  ?BBBY !BGGP.                          ..                                                                                            \r\n"
		   		+ "              :^: ?BGBY YBGB?                         ~G&&G                                                                                           \r\n"
		   		+ "            .YGBP.!BGGPJGGGGPY?~       .~J5GGGPY!    :&@@@@GGGGGGGGP^ 7PGP^   :!J5GGGPJ^:7YPGGG5?:    ~5GG7  ~5GGGGGGGGGGGG!  :!J5GGGP57:             \r\n"
		   		+ "            .PBGB?~BGGBBBGBBBBBB5:   ^5#@@@@@@@@@G:  5@@@@@@@@@@@@@&:J@@@@: ~P&@@@@@@@@@@@@@@@@@@&!  ~@@@@? :&@@@@@@@@@@@@P~?B@@@@@@@@@@@?            \r\n"
		   		+ "             !BGGGPGGG5?~^^!5GGGBY  Y@@@&P?7J#@@@@? !@@@@J77777777!.~@@@@?.P@@@&5?7J&@@@@BJ77P@@@@B .B@@@G   ~7!!?G@@@@@P~J&@@@BYJP@@@@@#7            \r\n"
		   		+ "              5BGGGGP~      :GGGBY 5@@@&^    Y@@@@^.#@@@P     .~!^  B@@@G G@@@B:    G@@@&:   ^@@@@Y Y@@@&^     ^Y#@@@#Y^ P@@@B~^YB@@@&5!.             \r\n"
		   		+ "              ~BGGGB!       ?BGBG:!@@@@7    ^&@@@J 5@@@#:    7&@@&.J@@@&:J@@@@^    !@@@@7    P@@@B ^@@@@J   :?B@@@@G!.  5@@@&:.&@@&P!.                \r\n"
		   		+ "               YBGGBY:...:: ~YP5:.#@@@P     5@@@@7^@@@@B.  :J@@@@7^@@@@?:@@@@J    .#@@@P    ~@@@@P.B@@@G  ~5&@@@#Y^     #@@@@?.~7!:                   \r\n"
		   		+ "               :PBBGBGPPPGG5^    5@@@&:     Y@@@@@&@@@@@&B#@@@@G~ B@@@G.G@@@B     5@@@&:    ^@@@@@&@@@@^^G@@@@@@#GBBBBB~J@@@@@#GGGGBJ                 \r\n"
		   		+ "                :?5GGGBGGPY?^   :@@@#!       J#@@@@@@@@@@@@&BY~  !@@@B:~@@@B^    :@@@#!      ~G&@@@@@&7!@@@@@@@@@@@@@@#^ 7G&@@@@@@@@?                 \r\n"
		   		+ "                   .:^^::.       ^!~.          ^~!!!!!!!!~^:     .~!^  .~!~.      ^!~.         :~!!!~. :!!!!!!!!!!!!!~     :~!!!!!!:                  \r\n"
		   		+ "");
			System.err.println("     				 ********************************************************************************");
			Thread.sleep(20);
			System.out.println("    				 ****************************  Atom Hotels Api   ********************************");
			Thread.sleep(20);
			System.err.println("    				 ********************************************************************************");
		   
		   } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}   
//				IHotelService hotelService = new HotelService();
//				HotelDao hotelDao =  new HotelDao();
//				DefaultOntimizeDaoHelper daoHelper = new DefaultOntimizeDaoHelper();
//				EntityResult resultado =  daoHelper.query(hotelDao,new HashMap<String, Object>(),
//						new ArrayList<String>() {{ add(HotelDao.ATTR_NAME);}});
			
	}
}
