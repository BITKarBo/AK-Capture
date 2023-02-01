# **AK-Capture for easy GIF recording**


AK-Capture software for capturing gifs on the fly.
Very easy to use and many options for perfect gifs.

https://bitkarbo.github.io/AK-Capture/

https://github.com/BITKarBo/AK-Capture

https://github.com/BITKarBo/FileServer_AK-Capture


**How to use:**
	
	Launch AK-Capture.jar
	You can find AK-Capture icon at icon tray.
	
	Right click for recording settings.
	
	Default keybindings:
		F8  -> Select area what to capture
		* ALT -> to move area "follow cursor" 
		* ESC -> cancel select
		
		F9  -> Fullscreen capture, start recording instantly.
		
		Releasing area selection start recording.
		Pressing F8/F9 again stops recording.
		
	
	After recording you can see icon change to buffering takes some time to finish compiling the gif.	

	Recordings can be found at output folder.
	
	After recording you can open gif from File Uploader hyper link.
	
	If you configure your file tranfer server, meaning IP, PORT and PATH + Password. 
	You can Upload your gif to your server and link automaticly copies to your clipboard.
	+ you can open gif in browser by clicking the link.
	
**Settings:**
		
	Settings can be found /res/cfg.config -- these settings override lauch arguments.
	You can also use launch arguments to change these settings.
	
	cfg.config:
		--followkey 18 					-> [ALT]
		--fullscreencapturekey 120 			-> [F9]
		--windowedcapturekey 119 			-> [F8]
		--pass [Password] 	-> For file tranfer server Secret.
		--ip [Addres] 		-> For file tranfer server URL.
		--port [Port]  		-> For file tranfer server Port.
		--path [Path]		-> For file tranfer server custom folder path.
		
	launch args:
		--menukey 18 					-> [ALT]
		--fkey 120 					-> [F9]
		--wkey 119 					-> [F8]
		--pass [Password] 	-> For file tranfer server Secret.
		--ip   [Addres] 	-> For file tranfer server URL.
		--port [Port]  		-> For file tranfer server Port.
		--path [Path]		-> For file tranfer server custom folder path.
		
	
	Example list of usable keycodes:
	
		112 -> [F1]  |  96  -> [NumPad-0]
		113 -> [F2]  |  97  -> [NumPad-1]
		114 -> [F3]  |  98  -> [NumPad-2]
		115 -> [F4]  |  99  -> [NumPad-3]
		116 -> [F5]  |  100 -> [NumPad-4]
		117 -> [F6]  |  101 -> [NumPad-5]
		118 -> [F7]  |  102 -> [NumPad-6]
		119 -> [F8]  |  103 -> [NumPad-7]
		120 -> [F9]  |  104 -> [NumPad-8]
		121 -> [F10] |  105 -> [NumPad-9]
		122 -> [F11] |  106 -> [NumPad-*]
		123 -> [F12] |  107 -> [NumPad-/]
		123 -> [F12] |  107 -> [NumPad-+]
	
	More codes https://cherrytree.at/misc/vk.htm
	
*** ENJOY - KarBo_ & Arska ***
