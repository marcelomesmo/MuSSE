# MuSSE

MuSSE is a tool developed to extract XML data from sprite sheet images with non-uniform – multi-sized – sprites.  
MuSSE (Multi-sized Sprite Sheet meta-data Exporter) is based on a Blob detection algorithm that incorporates a 
connected-component labeling system.  

 * More at:  
 http://www.sbgames.org/sbgames2015/anaispdf/computacao-full/147508.pdf  
 * Ever more at:  
 https://pdfs.semanticscholar.org/9f78/4d991f5902c84c2181c6c573661abdc228b1.pdf  
  
Welcome to MuSSE - Multi-sized Sprite Sheet meta-data Exporter.

Groovy adaptation.

 * JAR File at:  
Dropbox: https://www.dropbox.com/home/ifrn-marcelo/projetos/musse  
Direct Link: https://www.dropbox.com/s/bummz01sm2gfb6v/MuSSE.jar?dl=0  
  
 * HOW TO:  
 	1) Cut Selection (CTRL+S)  
 		1.1) Optional: Add Anchor Point (SHIFT+Click)  
 	2) Add Selection to Xml (CTRL+A) as a new set of Sprites (Animation)  
 	3) Export Xml (CTRL+X)  
 	4) Read Xml using parser  
   
 * XML format:  
 ![alt tag](https://raw.githubusercontent.com/marcelomesmo/MuSSE/master/xml-example.png)  
A Parser in C++ and Java (to read Musse's XML files) is available [here](https://github.com/marcelomesmo/MusseXmlParser).    
  	
 * TODO LIST:  
 	a) Make a editable list of cropped sprites/animations.  
  
 * Authors’ contact:  
 https://marcelomesmo.github.io  
 marcelo.barbosa (at) ifrn.edu.br  
   
MuSSE is distributed under the terms and conditions of the [zlib/png license](http://zlib.net/zlib_license.html).
