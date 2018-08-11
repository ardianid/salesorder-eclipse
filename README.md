# salesorder-eclipse

Sebuah aplikasi untuk pemesanan barang yang dilakukan sales melalui smartphone,

aplikasi sales order terbagi menjadi 2 bagian :
1. bagian sales, dimana saya menggunakan bahasa pemrograman java android (ide eclipse) dengan database sqlite offline dan sql server untuk online
dengan request json menggunakan php
2. bagian admin kantor untuk mencetak orderan menggunakan bahasa pemrograman vb.net dan sql server untuk database, 
   berfungsi memonitor orderan yang masuk (akan ada notifikasi apabila orderan masuk)

namun disini saya hanya upload bagian sales,

keunggulan dari aplikasi ini adalah :
1. sales tidak bisa input pemesanan barang apabila tidak ditoko yang bersangkutan (lock menggunakan gps dengan jarak tertentu)
2. bisa online dan offline (karena tidak semua area terdapat sinyal yang memadai, sales meyimpan orderan apabila gangguan/tidak ada sinyal dan mengirimkannya apabila sudah tersedia jaringan)
3. dapat mengajukan permohonan toko baru langsung dari aplikasi

server menggunakan web server sendri, dengan jaringan internet menggunakan provider m2, mikrotik untuk pembagian jaringan dan no-ip agar bisa diakses dari luar

team : 1 orang


