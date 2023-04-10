set term post eps enh "Arial" 32 color
set output "plot_load21600_pdf.eps"
set datafile missing "-"
set key inside top right Left
#set nokey

set auto x
#set xtic 20
set yrange [0:1.5]
#set grid y

set style line 1 lt 1 lc rgb "blue" lw 7
set style line 2 lt 2 lc rgb "purple" lw 7
set style line 3 lt 3 lc rgb "green" lw 7
set style line 4 lt 4 lc rgb "yellow" lw 7
set style line 5 lt 5 lc rgb "cyan" lw 7
set style line 6 lt 6 lc rgb "red" lw 7
set style line 7 lt 4 lc rgb "black" lw 7


set xlabel "Total Overhead"
set ylabel "PDF"

set style fill solid border -1
#set style data fillsteps
set style data lines
#set size 3,1
set xtic rotate by -30
plot 'simulation2_result.txt_6.dis.ndis' using 1:3 ti 'TimeMachine+Ckpt(Young)' ls 1, 'simulation2_4000_result.csv_6.dis.ndis' using 1:3 ti 'TimeMachine+Ckpt(4000)' ls 6, 'simulation1_young_result.txt_6.dis.ndis' using 1:3 ti 'Ckpt(Young)' ls 7, 'simulation1_2000_result.txt_6.dis.ndis' using 1:3 ti 'Ckpt(2000)' ls 3
