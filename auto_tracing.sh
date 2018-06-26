#!/bin/tcsh -f 
#
alias printl 'echo'
alias realpath 'pushd `dirname \!:1` > /dev/null ; echo ${cwd}/`basename \!:1` ; popd > /dev/null'
#
#set warpbin = $warpbintest ##### This line must be removed for the release
#
if ( $#argv == 0 || $1 == 'help' || $1 == 'h' ) goto usage
#
# Phaselabin labels should be checked for their presence in the MTZ file
# Phaselabin should separate into phaselabin and sadlabin
# keyword sad should go
#
# In this mode print default parameters to the console
if ( $1 == 'defaults' ) then
  set defaults = '1'
  if ( "$2" == '' ) then
    printl
    printl ' Usage: defaults parfile_to_update [keyword newargument]'
    exit(1)
  endif
  set parfile_in  = $2
  set newkey = ''
  set newarg = ''
  if ( "$3" != '' && "$4" != '' ) then
    set newkey = "$3"
    set newarg = "$4"
  endif
else
  set defaults = '0'
endif
#
# Print out the current time
printl
if ( $defaults == 0 ) then
  date
#
########## Check executability of every program needed, both in this sh file and all files that are called 
#
# Various checks for program executability, location and version
  if ( ! $?warpbin ) then
    printl
    printl 'Variable $warpbin is not defined - please check your ARP/wARP installation'
    goto exitlab
  endif
  if ( ! -d $warpbin ) then
    printl
    printl 'Directory '$warpbin' does not exist - please check your ARP/wARP installation'
    goto exitlab
  endif
#
  if ( "$argv[$#argv]" != 'nocheckprograms' ) then
    if ( ! -x $warpbin/check_programs.sh ) then
      printl
      printl 'ARP/wARP software "check_programs.sh" cannot be executed from the directory '
      printl $warpbin
      printl 'Please check your ARP/wARP installation'
      goto exitlab
    else
      $warpbin/check_programs.sh $0 $warpbin
      if ( $status ) goto exitlab
    endif
  endif
endif
#
# Array 'param_names' is to be output to par file as 'set $param_name[$i] = $arguments[$i]'
# Array 'arguments' contains default values. To change them add 'keyword argument' on the command line
# ; in array keywords indicates the values that cannot be changed from the command line (known_position flag, warpbin and version)
# ; in array arguments indicates compulsory keywords
# ;; in array arguments indicates white spaces
set keywords     = (defFile       jobId  projectName             workdir albe arpwarpdir   bcut1 bcut2 bcut3 buildingcycles )
set param_names  = (CCP4I_DEFFILE JOB_ID PROJECT                 WORKDIR albe arpwarpdir   bcut1 bcut2 bcut3 restrcyc       )
set stack_shift  = (2             2      2                       2       2    2            2     2     2     2              )
set arguments    = (UNDEFINED     ";;"   COMMAND_LINE_SUBMISSION ";;"    ";;" temp_tracing 2.0   2.0   2.0   10             )
#
set keywords     = ($keywords    cell               compareto cgr cycskip damp         datafile fakedata fbest flatten fom  fp )
set param_names  = ($param_names cell               compareto cgr skip    damp         datafile fakedata fbest flatten fom  fp )
set stack_shift  = ($stack_shift 2                  2         2   2       2            2        2        2     2       2    2  )
set arguments    = ($arguments   "1;;1;;1;;1;;1;;1" ";;"      1   0       '1.0;;1.0'   ;        "0;;0;;0" ";;" 0       ";;" FP )
#
set keywords     = ($keywords    freebuild freelabin fsig heavyin hmainpostfit is_semet keepdata keepjunk loops ;         )
set param_names  = ($param_names freebuild freelabin fsig heavyin hmainpostfit is_semet keepdata keepjunk loops modeccp4i )
set stack_shift  = ($stack_shift 2         2         2    2       2            2        2        2        2     2         )
set arguments    = ($arguments   0         ";;"      3.2  ";;"    1            0        WORLD    0        1     ";;"      )
#
set keywords     = ($keywords    modelin ;      multit ncsrestraints ncsextension nnuc parfile phaselabin phaseref phibest )
set param_names  = ($param_names modelin models multit ncsrestraints ncsextension nnuc parfile phaselabin phaseref phibest )
set stack_shift  = ($stack_shift 2       2      2      2             2            2    2       2          2        2       )
set arguments    = ($arguments   ";;"    1      5      ";;"          ";;"         0    1       ";;"       ";;"     ";;"    )
#
set keywords     = ($keywords    rand1 rand2 rand3 residues restraints ridgerestraints dipvali )
set param_names  = ($param_names rand1 rand2 rand3 protsize restraints ridgerestraints dipvali )
set stack_shift  = ($stack_shift 2     2     2     2        2          2               2 )
set arguments    = ($arguments   0     0     0     0        1          0               1 )
#
set keywords     = ($keywords    randshift1 randshift2 randshift3 randtimes refmax remote remoteemail resol restrref )
set param_names  = ($param_names randshift1 randshift2 randshift3 randtimes refmax remote remoteemail resol restrref )
set stack_shift  = ($stack_shift 2          2          2          2         2      2      2           2     2        )
set arguments    = ($arguments   '0.5'      '0.5'      '0.5'      0         MLKF   0      ";;"        ";;"  5        )
#
set keywords     = ($keywords    rrcyc rsig ;   scale  scaleopt             scalml       scanis seqin side sigfp solvent ;        ;   )
set param_names  = ($param_names rrcyc rsig sad scale  scaleopt             scalml       scanis seqin side sigfp solvent solventc sym )
set stack_shift  = ($stack_shift 2     2    2   2      2                    2            2      2     2    2     2       2        2   )
set arguments    = ($arguments   ";;"  1.0  0   SIMPLE "SIMPLE;;LSSC;;ANIS" "SCAL;;MLSC" Y      ";;"  '-1' SIGFP 1       0.5      1   )
#
set keywords     = ($keywords    twin upmore ;       sadcard arpipc   ;        weightv ;       wmat ;      deletedum )
set param_names  = ($param_names twin upmore version sadcard arpipc   warpbin  weightv wilsonb wmat xyzlim deletedum )
set stack_shift  = ($stack_shift 2    2      2       2       2        2        2       2       2    2      2         )
set arguments    = ($arguments   0    1      7.6     ";;"    ";;"     $warpbin ";;"    30.0    AUTO 1      0         )
#
set nkeywords    = $#keywords
#
# Updating to default values if requested
if ( $defaults ) then
#
# Reading in parfile to be updated
  set parin = `cat ${parfile_in}`
  set parin = `sed -e "s/'//g" -e "s/[^[:print:]]//g" ${parfile_in}`
  set inset = 0
  set imatch = 0
#
# Getting the keyword/arguments boundaries
  while ( $inset != $#parin )
    @ inset++
    @ inequ = $inset + 2
    @ inpar = $inset + 1
    @ inarg = $inequ + 1
    if ( $inequ <= $#parin ) then
      if ( $parin[$inset] == 'set' && $parin[$inequ] == '=' ) then
        @ imatch++
        if ( $imatch == 1 ) then
          set parinpar = $inpar
          set parinset   = $inset
          set parinarg1  = $inarg
        else
          set parinpar   = ( $parinpar $inpar )
          set parinset   = ( $parinset $inset )
          set parinarg1  = ( $parinarg1 $inarg )
        endif
      endif
    endif
  end
#  echo ' imatch' $imatch
  set parinarg2 = ( $parinarg1 )
  set i = 0
  while ( $i != ( $imatch - 1 ) )
    @ i++
    @ i2 = $i + 1
    @ parinarg2[$i] = $parinset[$i2] - 1
  end
  set parinarg2[$imatch] = $#parin
  set outlist = ''
#
# Make outlist. Check identity in parameter names 
# and exclude keywords ';' which cannot be input
  set iout = 0
  set inames = 0
  set jout = 0
  while ( $iout != $imatch )
    @ iout++
    set outlist = ( $outlist '' '' )
    set i = 0
    while ( $i != $nkeywords)
      @ i++
      if ( $param_names[$i] == $parin[$parinpar[$iout]] && $keywords[$i] != ';' ) then
        set iarg = 0
        if ( $parinarg2[$iout] == $parinarg1[$iout] ) then
          set iarg = 1
          @ jout++
          set outlist[$jout] = $keywords[$i]
          @ jout++
          set outlist[$jout] = $parin[$parinarg1[$iout]]
          @ inames++
        else if ( $parinarg2[$iout] > $parinarg1[$iout] ) then
          set iarg = 1
          @ jout++
          set outlist[$jout] = $keywords[$i]
          @ jout++
#
          set itemp1 = $parinarg1[$iout]
          set itemp2 = $parinarg2[$iout]
addagain:
          if ( $itemp1 == $parinarg1[$iout] ) then
            set outlist[$jout] = $parin[$itemp1]
          else
            set outlist[$jout] = ${outlist[$jout]}';'${parin[$itemp1]}
          endif
          if ( $itemp1 != $itemp2 ) then
            @ itemp1++
            goto addagain
          endif
          @ inames++
        endif
#
        if ( $iarg ) then
#
# Change jobId, workdir, modelin, datafile, compareto, seqin, 
#  heavyin, parfile (if provided) to the current directory
          if ( $keywords[$i] == 'jobId' ) set outlist[$jout] = '.'
          if ( $keywords[$i] == 'workdir' ) set outlist[$jout] = `pwd`
          if ( $keywords[$i] == 'modelin' || $keywords[$i] == 'datafile' || $keywords[$i] == 'compareto' || $keywords[$i] == 'seqin' || $keywords[$i] == 'heavyin' ) set outlist[$jout] = `pwd`/"$outlist[$jout]:t"
          if ( $keywords[$i] == 'parfile' ) then
#            set outlist[$jout] = "$outlist[$jout]:t"
            set outlist[$jout] = $parfile_in:t
            set outlist[$jout] = "$outlist[$jout]:r"_amended
          endif
#
#  Empty arpipc string
          if ( $keywords[$i] == 'arpipc' ) set outlist[$jout] = ';;'
#
# Making keyword $residues
          if ( $keywords[$i] == 'residues' ) then
            set ttt = `echo $outlist[$jout] | awk '{ print int($1/8) }'`
            set outlist[$jout] = $ttt
          endif
#
# Cleanup keyword $freelabin if given in a form FREE=freelabel
          if ( $keywords[$i] == 'freelabin' ) then
            set ttt = `echo $outlist[$jout] | sed -e 's|FREE=||'`
            set outlist[$jout] = $ttt
          endif
#
# Removing semicolons
#          set ttt = `echo $outlist[$jout] | sed -e 's|\;| |g'`
#          set outlist[$jout] = "'""$ttt""'"
#          echo $keywords[$i] $jout "$outlist[$jout]"
        endif
      endif
    end
  end
#
# Making keyword $buildingcycles
  set rrref = 0
  set ic = 0
  while ( $ic != $#outlist )
    @ ic++
    if ( "$outlist[$ic]" == 'restrref' ) then
      @ jc = $ic + 1
      set rrref = $outlist[$jc]
    endif
  end
  if ( $rrref != 0 ) then
    set ic = 0
    while ( $ic != $#outlist )
      @ ic++
      if ( "$outlist[$ic]" == 'buildingcycles' ) then
        @ jc = $ic + 1
        set ttt = `echo $outlist[$jc] $rrref | awk '{ print int($1/$2) }'`
        set outlist[$jc] = $ttt
      endif
    end
  endif
#
# Re-run auto_tracing.sh with the amended keywords and arguments. 
#  Keyword $parfile must be present in the list, therefore here we add it 
#  from the left: if it was there in defaults file - it will be taken, 
#  otherwise will be taken as OLD_PAR_FILE_amended.par
#  One keyword with new argument can be added for further update
#  The keyword arpwarpdir is set to temp_tracing and thus is added 
#  from the right.
  set ttt = $parfile_in:t
  set ttt = "$ttt:r"_amended
  if ( "$newkey" != '' ) then
    set nocheck = 'nocheckprograms'
  else
    set nocheck = ''
  endif
#  echo "$0 parfile $ttt $outlist $newkey $newarg arpwarpdir temp_tracing $nocheck"
  $0 parfile $ttt $outlist "$newkey" "$newarg" arpwarpdir temp_tracing $nocheck
  if ( $status ) then
    echo 'ERROR STATUS SCRIPT_auto_tracing.sh'
    exit(1)
  else
    exit
  endif
endif
#
# Outer loop over the command line arguments
parse_the_input:
#
# Inner loop over the keywords
set count = 0
set keyword_found = 0
set keyword_ignored = ''
#echo ' parse input anew' $1
foreach try ( $keywords )
  @ count++
#
# Check whether the keyword matches the first word in the argument list (unless this is a dummy keyword ;)
#echo 'dollar-1 try' "$1" $try 
#echo $argv
  if ( $1 == $try && $try != ';' ) then
    set i = 1
    while ( $i < $stack_shift[$count] )
#      echo 'dollar-2 try' "$2" $try $i $stack_shift[$count]
      if ( "$2" == '' ) then
        echo 'Keyword "'"$1"'" is given with no argument(s)'
        goto usage
      endif
      if ( $i == 1 ) then
        set arguments[$count]    = "$2"
      else
        set arguments[$count]    = "$arguments[$count] $2"
      endif
      shift
      @ i++
    end
    shift
    set keyword_found = 1
  endif
end
if ( $keyword_found == 0 ) then
  echo ' Ignored keyword      "'$1'"'
  shift
endif
if ( $#argv != 0 ) goto parse_the_input
#
# Check for compulsory keywords
set i = 1
foreach try ( $keywords )
#  echo 'keyword '$try' argument '$arguments[$i]
  if ( "$arguments[$i]" == ';' ) then
    printl
    printl 'Missing compulsory keyword: '$keywords[$i]
    goto usage
  endif
  @ i++
end
#
# Processing parameter names and their index offsets
set i = 1
foreach try ( $param_names )
  set index_$try = $i
  @ i++
end
#
# Changing ;; to spaces in the arguments array
set i = 1
foreach try ( $keywords )
  if ( "$arguments[$i]" == ';;' ) then
    set arguments[$i] = ''
  else
    set ttt = `echo $arguments[$i] | sed -e 's|;| |g'`
    set arguments[$i] = "$ttt"
  endif
  @ i++
end
#
# Setting workdir to the current directory if not given on input. Check for its existance
if ( "$arguments[$index_WORKDIR]" == '' ) set arguments[$index_WORKDIR] = `pwd`
if ( ! -d "$arguments[$index_WORKDIR]" ) then
  printl 'Directory '$arguments[$index_WORKDIR]' does not exist'
  goto usage
endif
printl
printl ' Working directory    '$arguments[$index_WORKDIR]
#
# Check that WORKDIR does not have a space
set cindex1 = `echo $arguments[$index_WORKDIR] | sed -e 's| |\;|g' | awk '{print index($0,";")}'`
if ( $cindex1 != 0 ) then
  printl ' Illegal name for the Working directory - contains a white space'
  goto usage
endif
#
# Setting the Job Id
# If jobId is given on input and it is not '.', then it will appear as the
# prefix for the output file
if ( "$arguments[$index_JOB_ID]" == '' ) then
  set arguments[$index_JOB_ID] = `date +%Y%m%d_%H%M%S`
  set fileprefix = 0
else
  set fileprefix = 1
endif
printl ' Job ID is set to     '$arguments[$index_JOB_ID]
if ( "$arguments[$index_JOB_ID]" == '.' || "$arguments[$index_JOB_ID]" == './' ) then
  set jobid = ''
  set fileprefix = 0
else
  set jobid = '/'"$arguments[$index_JOB_ID]"
endif
#
# Setting the datafile. Check for its existance
set ftest = $index_datafile
set file_namecheck_message = ' X-ray data file      '
source $warpbin/incl_check_filename.csh
if ( $gotousage ) goto usage
#
# Setting $side and $seqin if provided. Check for its existance
# Also keep $side if given on the command line (the cycle number to start sequence docking)
set ftest = $index_seqin
if ( "$arguments[$ftest]" != '' ) then
  set file_namecheck_message = ' Sequence file        '
  source $warpbin/incl_check_filename.csh
  if ( $gotousage ) goto usage
#
  if ( "$arguments[$index_side]" == '-1' ) then
    set arguments[$index_side] = '1'
  endif
else
  set arguments[$index_side] = '-1'
#
# Turn loopy off
  set arguments[$index_loops] = '0'
endif
#
# Setting $modelin if provided. Check for its existance. Set $modeccp4i
set ftest = $index_modelin
if ( "$arguments[$ftest]" != '' ) then
  set file_namecheck_message = ' Input PDB file       '
  source $warpbin/incl_check_filename.csh
  if ( $gotousage ) goto usage
#
  set arguments[$index_modeccp4i] = 'WARPNTRACEMODEL'
else
  set arguments[$index_modeccp4i] = 'WARPNTRACEPHASES'
endif
printl ' Tracing mode         '$arguments[$index_modeccp4i]
#
# Setting the compareto file if provided. Check for its existance
set ftest = $index_compareto
if ( "$arguments[$ftest]" != '' ) then
  set file_namecheck_message = ' Compareto file       '
  source $warpbin/incl_check_filename.csh
  if ( $gotousage ) goto usage
endif
#
# Setting the heavyin file if provided. Check for its existance
set ftest = $index_heavyin
if ( "$arguments[$ftest]" != '' ) then
  set file_namecheck_message = ' Heavyin file         '
  source $warpbin/incl_check_filename.csh
  if ( $gotousage ) goto usage
endif
#
# Here we create working subdirectory, remove double slash if given in the input
set workdir = `echo ${arguments[$index_WORKDIR]}${jobid} | sed -e 's/\/\//\//g'`
set arguments[$index_WORKDIR] = $workdir
if ( ! -e ${workdir} ) then 
  printl
  printl ' Creating directory  ' ${workdir}
  mkdir ${workdir}
  if ( $status ) then 
    printl ' Could not create job subdirectory '$workdir
    goto usage
  endif
endif
#
# Check mtzfile labels
set lastprog = MTZLABELS_CHECK_LABELS
set lastlogfile = ${workdir}/arp_mtzlabels.log
$warpbin/mtzlabels hklin $arguments[$index_datafile] > $lastlogfile
if ( $status ) goto exitlab
## If jobId is given on input and it is not '.', then it will appear as the
# prefix for the output file

printl
if ( $arguments[$index_modeccp4i] == 'WARPNTRACEPHASES' ) then
  if ( "$arguments[$index_fbest]" != '' ) then
    set labels_to_check    = ( fp         sigfp         fbest        phibest        freer )
    set labels_indexes     = ( $index_fp  $index_sigfp  $index_fbest $index_phibest $index_freelabin )
    set labels_to_grep     = ( FOBSLABELS SIGFOBSLABELS FOBSLABELS   PHILABELS      FREELABELS )
    set labels_compulsory  = ( 1          1             1            1              0 )
    set arguments[$index_fom] = ''
  else
    set labels_to_check    = ( fp         sigfp         phibest        fom        freer )
    set labels_indexes     = ( $index_fp  $index_sigfp  $index_phibest $index_fom $index_freelabin )
    set labels_to_grep     = ( FOBSLABELS SIGFOBSLABELS PHILABELS      FOMLABELS  FREELABELS )
    set labels_compulsory  = ( 1          1             1              1          0 )
  endif
else
  set labels_to_check    = ( fp         sigfp         phibest          fom        freer )
  set labels_indexes     = ( $index_fp  $index_sigfp  $index_phibest   $index_fom $index_freelabin )
  set labels_to_grep     = ( FOBSLABELS SIGFOBSLABELS PHILABELS        FOMLABELS  FREELABELS )
  set labels_compulsory  = ( 1          1             2                2          0 )
endif
set labels_fail     = 0
set labels_taken    = ''
set ic = 0
foreach mtzlabel ( $labels_to_check )
  @ ic++
  set labels_grep = $labels_to_grep[$ic]
  set labels_test = (`grep $labels_grep $lastlogfile | head -1 | sed -e 's/END'$labels_grep'//g' | sed -e 's/'$labels_grep'//g'`)
  set labels_match = '0'
  foreach try ( $labels_test )
    if ( $try == "$arguments[$labels_indexes[$ic]]" ) then
      set labels_match = '1'
      if ( ! $labels_fail ) then
        printl ' Accepted label:      '$param_names[$labels_indexes[$ic]]'='$try
      endif
    endif
  end
#
# For no match: if there is only one label in mtz - take it, otherwise stop with an error for compulsory labels
  set nlabels_test = $#labels_test
  if ( ! $labels_match ) then
    if ( $labels_compulsory[$ic] ) then
#
# Stop if compulsory label does not match, treat compulsory=2 differently
      if ( $nlabels_test != 1 ) then
        if ( $labels_compulsory[$ic] != 2 ) then
          set labels_fail = '1'
          printl
          printl 'Compulsory label '$param_names[$labels_indexes[$ic]]' is not assigned or does not match the content of the datafile '$arguments[$index_datafile]
          if ( $nlabels_test == 0 ) then
            printl 'mtz file does not contain appropriate label(s)'
          else
            printl 'Possible mtz labels are: '$labels_test
          endif
        else
          if ( $nlabels_test > 1 ) then
            set nlabels_test = 1
          endif
        endif
      endif
#
# Assign compulsory labels by default
      if ( $labels_fail != '1' && $nlabels_test == '1' ) then
        set labels_match = '1'
        set arguments[$labels_indexes[$ic]] = $labels_test[1]
        printl ' Guessed label:       '$param_names[$labels_indexes[$ic]]'='$labels_test[1]
      endif
    else
#
# This is not matched not compulsory label. Check whether it is assigned from input. If yes - clean up
#      echo '-- arguments --'$arguments[$labels_indexes[$ic]]
      if ( "$arguments[$labels_indexes[$ic]]" != '' ) then
        echo ' Data column '"$arguments[$labels_indexes[$ic]]"\
         ' is not present in the MTZ file or is not of the right type to assign to '\
         "$param_names[$labels_indexes[$ic]]"
        echo
        set arguments[$labels_indexes[$ic]] = ''
      endif
    endif
  endif
#
  if ( $labels_match ) then
    set labels_taken = ( $labels_taken $arguments[$labels_indexes[$ic]] )
  endif
end
#
if ( $labels_fail == 1 ) goto usage
echo ' mtz labels taken:    '$labels_taken
#
# Setting the sad and sadcard keywords
if ( "$arguments[$index_sadcard]" != '' ) then
  set arguments[$index_sad] = '1'
  if ( "$arguments[$index_phaselabin]" == '' || "$arguments[$index_heavyin]" == '') then
    echo ' '
    echo ' *** WARNING *** For SAD refinement sadcard, phaselabin and heavyin must be provided'
    echo ' *** WARNING *** SAD option is now disabled'
    echo ' ' 
    set arguments[$index_heavyin] = ''
    set arguments[$index_sadcard] = ''
    set arguments[$index_sad] = '0'
  else
    echo ' '
    echo ' SAD refine labels    '"$arguments[$index_phaselabin]"
    echo ' SAD refine option    '"$arguments[$index_sadcard]"
  endif
endif
#
# Making up FreeR lable. 
if ( "$arguments[$index_freelabin]" != '' ) then
  set freetemp = 'FREE='$arguments[$index_freelabin]
  set arguments[$index_freelabin] = $freetemp
endif
#
# For modelin clean up fom/phib labels
if ( $arguments[$index_modeccp4i] == 'WARPNTRACEMODEL' ) then
  if ( $arguments[$index_fom] == '' && $arguments[$index_fbest] == '' ) then
    set arguments[$index_fbest] = $arguments[$index_fp]
  endif
#  set arguments[$index_phibest] = ''
#  set arguments[$index_fom] = ''
endif
#
# Get Resolution and space group
set ftest = `grep 'Cell parameters' $lastlogfile | awk '{print $3,$4,$5,$6,$7,$8}'`
set arguments[${index_cell}] = "$ftest"
if ( "$arguments[${index_resol}]" == '' ) then
  set ftest = `grep 'Resolution range' $lastlogfile | awk '{print $3,$4}'`
  set arguments[${index_resol}] = "$ftest"
endif
set arguments[$index_sym] = `grep 'Space group number' $lastlogfile | awk '{print $4}'`
#/bin/rm -r $lastlogfile
#
# Get Wilson B and solvent content
set lastprog = ARP_WARP_MODE_WILSON
set lastlogfile = ${workdir}/warp_wilson.log
$warpbin/arp_warp > $lastlogfile <<EOF
MODE WILSON NRES $arguments[$index_protsize]
FILES CCP4 HKLIN $arguments[$index_datafile]
RESOLUTION $arguments[$index_resol]
LABELS FP=$arguments[$index_fp] SIGFP=$arguments[$index_sigfp] FUSE=$arguments[$index_fp]
END
EOF
if ( $status) then
  set solconerror = `grep "Computed solvent content is too" $lastlogfile | wc -l`
  if ( $solconerror == '0' ) then
    goto exitlab
  endif
endif
#
set arguments[$index_wilsonb] = `grep 'Bfactor' $lastlogfile | awk '{print $4}'`
set arguments[$index_solventc] = `grep 'Solvent content' $lastlogfile | awk '{print $3}'`
#
# Set $protsize
set ttt = `echo $arguments[$index_protsize] | awk '{ print $1*8 }'`
set arguments[$index_protsize] = $ttt
#
# Setting remote flags
if ( "$arguments[$index_remoteemail]" != '' ) then
  set arguments[$index_remote] = '1'
else
  set arguments[$index_remote] = '0'
endif
#
# Setting $rrcyc for the number of refmac cycles
if ( "$arguments[$index_rrcyc]" == '' ) then
  set arguments[$index_rrcyc] = `echo $arguments[$index_resol] | awk '{ if ($2 >= 2.3) print "3" ; else print "1"}'`
endif
#
# Setting $albe to space first (in case of input 'able no'), then to 1 or 0
if ( "$arguments[$index_albe]" != 0 && "$arguments[$index_albe]" != 1 ) then
  set arguments[$index_albe] = ''
endif
if ( "$arguments[$index_albe]" == '' ) then
  set arguments[$index_albe] = `echo $arguments[$index_resol] | awk '{ if ($2 >= 2.7) print "1" ; else print "0"}'`
endif
#
# Setting ncsrestraints 
if ( "$arguments[$index_ncsrestraints]" == '' ) then
  set arguments[$index_ncsrestraints] = `echo $arguments[$index_resol] | awk '{ if ($2 >= 1.5) print "1" ; else print "0"}'`
endif
#
# Setting ncsextension 
if ( "$arguments[$index_ncsextension]" == '' ) then
  set arguments[$index_ncsextension] = `echo $arguments[$index_resol] | awk '{ if ($2 >= 1.5) print "1" ; else print "0"}'`
endif
#
# Setting the total number of cycles
set maxbuildingcycles = '100'
if ( "$arguments[$index_restrcyc]" > $maxbuildingcycles ) then
  echo
  echo ' The number of building cycles will be truncated to '$maxbuildingcycles
  echo
  set arguments[$index_restrcyc] = $maxbuildingcycles
endif
set ftest = `echo $arguments[$index_restrcyc]' '$arguments[$index_restrref] | awk '{ if ($1 <= 0) print $2 ; else print $1*$2 }'`
set arguments[$index_restrcyc] = "$ftest"
#
# Making up hmainpostfit label. 
if ( "$arguments[$index_hmainpostfit]" != '' ) then
  set ftest = $arguments[$index_hmainpostfit]
  set arguments[$index_hmainpostfit] = "$ftest"
endif
#
# Checking that $sadcard and $twin cannot be given both at the same time
if ( ( "$arguments[$index_sadcard]" != '' ) && ( $arguments[$index_twin] != '0' ) ) then
  printl 
  printl ' *** WARNING *** SAD and TWIN options cannot be used together'
  printl ' *** WARNING *** TWIN option has been disabled'
  printl 
  set arguments[$index_twin] = '0'
endif
#
# Checking that $phaselabin and $twin cannot be given both at the same time
if ( "$arguments[$index_phaselabin]" != '' && $arguments[$index_twin] ) then
  printl 
  printl ' *** WARNING *** PHASERESTRAINTS and TWIN options cannot be used together'
  printl ' *** WARNING *** TWIN option has been disabled'
  printl 
  set arguments[$index_twin] = '0'
endif
#
# Get xyzlim, do NOT check the status
set lastprog = ARP_WARP_EXTRACT_AULIM
set lastlogfile = ${workdir}/arp_aulim_extract.log
$warpbin/arp_warp > $lastlogfile <<EOF
MODE MIRBUILD
SYMM $arguments[$index_sym]
END
EOF
#
set ftest = `grep 'Mapxyzlim' $lastlogfile | awk '{print $2,$3,$4,$5,$6,$7}'`
set arguments[${index_xyzlim}] = "$ftest"
/bin/rm -r $lastlogfile
#
# Setting parameter file name
if ( $arguments[$index_parfile] == 1 ) then
  set joblaunch = '1'
  set nameshort = 'arp_warp_tracing'
else
  set joblaunch = '0'
  set nametail  = $arguments[$index_parfile]:t
  set nameshort = `printl $nametail:r`
endif
set parfile = ${workdir}/${nameshort}'.par'
set arguments[$index_parfile] = $parfile
#
# Resetting the jobid name unless provided on input
if ( $fileprefix == 0 ) then
  set nametail  = $arguments[$index_datafile]:t
  set arguments[$index_JOB_ID] = `printl $nametail:r`
endif
#
# Writing out parameter file
set partemp = ${workdir}${jobid}.tmp
if ( -e ${partemp} ) /bin/rm -f ${partemp}
touch ${partemp}
set i = 0
while ( $i != $nkeywords )
  @ i++
  set argout = "$arguments[$i]"
#
  set cindex1 = `echo $argout | awk '{print index($0," ")}'`
  set cindex2 = `echo $argout | awk '{print index($0,"=")}'`
  set cindex3 = `echo $argout | awk '{print index($0,"@")}'`
  set cindex4 = `echo $argout | awk '{print index($0,"(")}'`
  set cindex5 = `echo $argout | awk '{print index($0,")")}'`
  if ( $cindex1 > 0 || $cindex2 > 0 || $cindex3 > 0 || $cindex4 > 0 || $cindex5 > 0 ) set argout = "'""$argout""'"
#
  printl 'set '${param_names[$i]}' = '"$argout" >> ${partemp}
end
#
# Sorting par file
sort ${partemp} >& $parfile
if ( $status ) cp ${partemp} $parfile
/bin/rm -f ${partemp}
#
printl
printl ' Parameter file       '$parfile
#
if ( $joblaunch == 1 ) then
  printl ' Job launched in      '$workdir 
#
  set lastprog = $warpbin/warp_tracing.sh
  set lastlogfile = 'where the output of warp_tracing.sh went to'
  $warpbin/warp_tracing.sh $parfile 1
  if ( $status ) goto exitlab
#
  printl
  printl ' Job finished.'
endif
#
printl
exit
#
exitlab:
#
# Printout failure of a module
if ( ! $?lastprog ) set lastprog = $0
printl
printl 'QUITTING ... ARP/wARP module stopped with an error message:'
printl ${lastprog}
printl
if ( ! $?lastlogfile ) then
else
  printl '*** Look for error message in the file: '
  printl ${lastlogfile}
  printl
endif
exit(1)
#
usage:
#
printl 
printl "Usage:"
printl "auto_tracing.sh                                                                     \"
printl "     datafile {mtzfile}                                                             \"
printl "     [residues {number_of_residues_in_AU}]                                          \"
printl "     [workdir {FULLPATH_WORKING_DIRECTORY}]                                         \"
printl "     [jobId {name_of_working_subdirectory_and_fileprefix,                           \"
printl "       default is YYYYMMDD_HHMMSS and no file prefix}]                              \"
printl "     [fp {fp_label}] [sigfp {sigfp_label}] [freelabin {freer_label}]                \"
printl "     [fbest {weighted_amplitude_label}] [phibest {phibest_label}] [fom {fom_label}] \"
printl "     [modelin {input_PDB_file_to_use_as_initial_model}]                             \"
printl "     [seqin {sequence_file_for_one_NCS_copy}]                                       \"
printl "     [cgr {number_of_NCS_copies (if seqin is provided, default is 1) }]             \"
printl "     [buildingcycles {the_number_of_autobuilding_cycles (default is 10) }]          \"
printl "     [resol {'rmin rmax' (default is the full resolution range) }]                  \"
printl "     [albe {1 to always invoke albe, default is 0 for resol < 2.7A, else 1) }]      \"
printl "     [restraints {1 to use conditional restraints, default is 1 }]                  \"
printl "     [twin {1 to try de-twining and twin refinement, default is 0 }]                \"
printl "     [compareto {PDB_file_for_comparison}]                                          \"
printl "     [keepjunk {1 to keep intermediate models, default is 0 ]                       \"
printl "     [parfile {parfilename_if_only_parfile_is_to_be_created}]                       \"
printl 
printl " - Optional command line arguments are given in square parentheses"
printl " - Possible combinations of MTZ labels are:"
printl "     For start from phases:"
printl "       fp/sigfp/phibest/fom or fbest/sigfp/phibest to build initial free-atoms model"
printl "       and fp/sigfp to refine the model"
printl "       If 'fbest' is given, 'fom' will be ignored"
printl "     For start from a model:"
printl "       fp/sigfp to refine the model"
printl
printl " - All input files are assumed to be located in working directory"
printl "   unless they are given with full path"
printl " - If workdir is not given, the current directory will be assumed"
printl " - All output files will be written into workdir/subdirectory"
printl 
printl 
printl "Additional useful tips:"
printl " - Normally the job runs in a subdirectory called YYYYMMDD_HHMMSS"
printl "   To run the job in the current directory use: auto_tracing.sh jobId '.'"
printl "   To run the job in the directory 'myjob' use: auto_tracing.sh jobId myjob"
printl " - If you have a par file from an earlier version of ARP/wARP and would like to"
printl "   re-run that job now, use: auto_tracing.sh defaults OLD_PAR_FILE"
printl "   This will create a par file compatible with the current ARP/wARP version"
printl "   and the keywords, which are new to OLD_PAR_FILE will take their default values"
printl " - To carry out phase-restrained Refmac refinement please provide"
printl "   phaselabin in a form of either ' HLA=HLAM HLB=HLBM HLC=HLCM HLD=HLDM ' "
printl "   or ' PHIB=PHIM FOM=FOMM ', together with keyword phaseref ' PHAS SCBL 1.0 '"
printl " - To carry out SAD refinement in Refmac please provide"
printl "   - phaselabin in a form of ' F+=F+ SIGF+=SIGF+ F-=F- SIGF-=SIGF- '"
printl "   - sadcard in a form of ' ANOM FORM SE -7.0 6.5 ' or ' ANOM WAVE 0.98 ' "
printl "   - heavyin PDB_FILENAME with heavy atom coordinates"
printl " - If you invoke auto_tracing.sh from another script and the keywords with"
printl "   many-word argument are not properly understood, e.g. resol '20.0 2.5',"
printl "   try resol 20.0;2.5 or resol '20.0;2.5'"
printl " - NCS-based chain extension and NCS restraints with Refmac are applied"
printl "   automatically if the resolution of the data is equal to or lower than 1.5 A."
printl "   Input 'ncsextension 1/0' to apply / not apply NCS extension regardless of the "
printl "   resolution of the data. Input 'ncsrestraints 1/0' has similar effect"
printl
exit(1)
