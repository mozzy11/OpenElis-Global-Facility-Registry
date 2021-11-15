#!/usr/bin/perl -w
use File::Copy;
use File::stat;
use Cwd;

sub getTimeStamp {
	( $sec, $min, $hour, $mday, $mon, $year, $wday, $yday, $isdst ) =  localtime time ;
	my $fullYear  = $year + 1900;
	my $fullMonth = $mon + 1;

	return  "_" . $mday . "_" . $fullMonth . "_" . $fullYear . "_" . $hour . "_" . $min;
}

sub deleteOverAgedBackups {
	my $maxTimeSpan    = shift;
	my $cumulativeDir = shift;

	chdir $cumulativeDir;

	@$files = <*>;
	foreach my $file (@$files) {
		if ( time - $maxTimeSpan > stat($file)->mtime ) {
			unlink($file);
		}
	}
}

sub sendOffsite{
	my $queueDir = shift;
	my $upLoadtargetURL    = shift;
	my $upLoadUserName = shift;
	my $upLoadPassword = shift;
	
	my $maxRetryCount = 3;
	my $curlExe = 'curl';

	chdir "$queueDir";

	my @files = <$queueDir/*.backup.gz>; 

	foreach $file (@files) {
   		my $command = $curlExe . ' -k --user ' . $upLoadUserName . ':' . $upLoadPassword
   					. ' --url ' . $upLoadtargetURL 
   					. ' --form "dataFileName=@' . $file . '"';
		#print $command . "\n";
  		my $retryCount = 0;
   		my $sendSuccess = 0; #false
           
   		while ($retryCount < $maxRetryCount) {
   			my $curlReturn = `$command`;
        	my $returnStatus = $?;
        	
        	if (($returnStatus != 0) || ($curlReturn ne 'success')) {
        		print "Curl had an error. Curl said \n$curlReturn\n"
        				. "Return status $returnStatus\n";
        		$retryCount = $retryCount + 1;
        	} else {
        		$sendSuccess = 1; #true
        		last;
        	}
        	
        	sleep 7;
		}
           
		if ($sendSuccess) {
    		#remove file from system
    		unlink( $file );
    	}
	}           
}

my $postgres_pwd  = '[% postgres_password %]';
my $keepFileDays  = 30;
my $siteId = '[% siteId %]';
my $upLoadtargetURL = 'https://openelis-recv.cirg.washington.edu/receive-file/receive-file.pl';
my $upLoadUserName = 'receive-file';
my $upLoadPassword = 'ac5pxvkn2';

my $snapShotFileBase     = 'lastSnapshot_' . $siteId; 
my $snapShotFileName     = $snapShotFileBase . '.backup'; 
my $snapShotFileNameZipped     = $snapShotFileName . '.gz'; 
my $cmd = 'pg_dump -h localhost  -U clinlims -f "' . $snapShotFileName . '" -n \"clinlims\" clinlims';
my $zipCmd = 'gzip -f ' .  $snapShotFileName;
my $backBaseDir          = cwd();
my $baseFileName         = '[% installName %]';
my $dailyDir             = "$backBaseDir/daily";
my $cumulativeDir        = "$backBaseDir/cumulative";
my $queueDir             = "$backBaseDir/transmissionQueue";
my $timeStamp            = getTimeStamp();
my $todaysCummlativeFile = "$siteId$baseFileName$timeStamp.backup.gz";
my $maxTimeSpan = 60 * 60 * 24 * $keepFileDays;

$ENV{'PGPASSWORD'} = "$postgres_pwd";

chdir "$dailyDir";
my $response = system("$cmd")  and warn "Error while running: $! \n";
system("$zipCmd")  and warn "Error while running: $! \n";

copy( $snapShotFileNameZipped, "$cumulativeDir/$todaysCummlativeFile" ) or die "File cannot be copied.";
copy( $snapShotFileNameZipped, "$queueDir/$todaysCummlativeFile" ) or die "File cannot be copied.";

deleteOverAgedBackups ($maxTimeSpan, $cumulativeDir);

sendOffsite($queueDir, $upLoadtargetURL, $upLoadUserName, $upLoadPassword);

   









