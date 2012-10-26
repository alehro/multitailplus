multitailplus
=============

multi tail plus multi out with regex

It is advanced analog of 'tail -f' linux command. It is advanced in such a ways:
- Multiple input files are possible.
- Multiple output files are possible. Output can be filtered and written to multiple output files. I.e. you may write lines containing 'ERROR' to one file,
'DEBUG' to second one, '<custom regex>' to third one, etc.
- Configuration is done in JSON. See work/config.json for example.