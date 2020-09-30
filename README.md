# CA_sem4_bloodCellAnalyser
Sem4assignment1
by dimakis

The objective of this CA exercise was to create a JavaFXapplication that can be supplied with an image of a prepared/stained microscopic blood sample showing 
blood cells. When given an image, the app can automatically locate individual (and clusters of) blood cells and estimate how many of them are in the image overall.
It can also differentiate red and white blood cells. It highlights the different cells with green rectangles for white blood cells, blue rectangles for clusters
of white blood cells and red blood cells in purple rectangles.

It does this by converting the image to a tri color version. The application gives the user options to tune the image to optmize the tri-color conversion. Each 
pixel in the tricolor image is considered a disjoint-set, with this information represented in an array. Union-find is then applied to adjacent pixels to identify
individual cells and clusters. 

Some other features include sequentially numbering of the cells, customizable levesls of image noise reduction and outlier managment
along with the counting of individual cells in clusters.
