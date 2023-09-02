# filter-funnies

Catching messages with banned words and some stats.

## Description
Loaded some tsv files as messages.

You can play around, create new functions for filtering, etc.

## Usage

java version >= 8

```bash
javac *.java
java BigBrother
```

## Outputs
>BigBrother bb = new BigBrother();
>System.out.println(bb.getSenderMessageCount("gear4", "2012-04-09"));
>> 52
> 
> System.out.println(bb.getUniqueReceivers("2012-04-09"));
>> [gear4, sacarlson]
> 
> System.out.println(bb.getTopReceiversBySender("sacarlson", 10));
>> {gear4=41}
> 
> System.out.println(bb.getSentReceivedRatio());
>> {ebaby=0.8301886792452831,
psycho_oreos=0.94,
Tsjoklat=0.6440677966101694,
UbuntuServerUser=0.8653846153846154,
...
Seveas=0.6851851851851852,
Fl-i-nT=0.9019607843137255,
outbackwifi=1.108695652173913,
glaceman=0.9795918367346939}
