#!/usr/bin/env ccp4-python

import argparse
import os
import sys

parser = argparse.ArgumentParser(description='Runs the buccaneer i2 pipeline')
parser.add_argument('--mtzin', required=True, metavar='FILE', help='required')
parser.add_argument('--seqin', required=True, metavar='FILE', help='required')
parser.add_argument('--colinfo', default='F,SIGF', metavar='COLS', help='default=F,SIGF')
parser.add_argument('--colinhl', default='HLA,HLB,HLC,HLD', metavar='COLS', help='default=HLA,HLB,HLC,HLD')
parser.add_argument('--colinfree', default='FreeR_flag', metavar='COL', help='default=FreeR_flag')
parser.add_argument('--iterations', type=int, default=5, metavar='N', help='default=5')
parser.add_argument('--add-waters', action='store_true')
parser.add_argument('--mtz-name', type=str,default='1', metavar='N', help='required')
args = parser.parse_args()
#sys.path.append(os.path.join(os.environ["CCP4"],"share","ccp4i2"))
#print (os.path.join(os.environ["CCP4"],"share","ccp4i2"))
#sys.path.append(os.environ["CCP4"])
#sys.path.append("/Applications/ccp4-7.0/share/ccp4i2")
#sys.path.append("/Users/emadalharbi/devel")
sys.path.append("ccp4i2Core")
print sys.path
from core import CCP4XtalData
from PluginUtils import smartSplitMTZ, makeASUContentFile, Runner
print "os.getcwd() "
print args.mtz_name
os.mkdir(args.mtz_name)
dbFile=os.path.join(os.getcwd()+"/"+args.mtz_name, "db.sqlite")
runner = Runner(dbFile=dbFile)

cOpenJob = runner.jobForTaskInProjectName(projectName="project"+args.mtz_name, taskName="buccaneer_build_refine_mr")

jobDirectory = runner.jobDirectory(cOpenJob)
print "JobDir#" + jobDirectory
inputData = cOpenJob.container.inputData
controlParameters = cOpenJob.container.controlParameters

inputSequences = [{"seqFile":args.seqin,"nCopies":1, "skipLines":1}]

inputData.F_SIGF = smartSplitMTZ(args.mtzin, '/*/*/['+args.colinfo+']', intoDirectory=jobDirectory)
inputData.ABCD = smartSplitMTZ(args.mtzin, '/*/*/['+args.colinhl+']', intoDirectory=jobDirectory)
inputData.FREERFLAG = smartSplitMTZ(args.mtzin, '/*/*/['+args.colinfree+']', intoDirectory=jobDirectory)
inputData.ASUIN = makeASUContentFile(inputSequences, intoDirectory=jobDirectory)

controlParameters.BUCCANEER_PHSIN_TYPE = 'experimental'
controlParameters.ITERATIONS = args.iterations
#controlParameters.COOT_REALSPACE_OPERATION = 'coot_add_waters' if args.add_waters else 'none'
controlParameters.COOT_REALSPACE_OPERATION ='none'
jobQProcess = runner.setRunning(cOpenJob)
jobQProcess.waitForFinished(-1)
