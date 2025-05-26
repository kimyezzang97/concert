## Kafka 공부

### Kafka란?
서비스 간 결합도를 낮추고, 고가용성 환경에서 데이터 손실 없이 대량의 데이터를 안정적으로 처리할 수 있는 분산 메시지 시스템입니다

### Producer & Consumer
- Producer - 메세지를 카프카 브로커에 적재하는 서비스
- Consumer - 카프카 브로커에게 적재된 메세지를 읽어오는 서비스
  - 메세지를 읽을 때마다 파티션 별로 offset을 유지해 처리했던 메시지의 위치를 추적합니다.
  - **current-offset**
    - 컨슈머가 어디까지 처리했는지를 나타내는 offset입니다.
    - 만약 오류가 발생할 경우 --reset-offsets 옵션을 통해 특정 시점으로 offset을 되돌릴 수 있습니다.

### Cluster란?
Cluster는 여러개의 브로커로 구성된 시스템입니다. Broker는 각각의 서버 인스턴스로 볼 수 있습니다. 
그리고 이 Cluster를 관리하는 용도로 주키퍼 클러스터(앙상블)가 필요합니다.(2.8 이후부턴 Zookeeper 없이 동작 가능한 KRaft 모드도 쓸 수 있다고 합니다.)

### Broker란?
Kafka가 설치되어 있는 서버 인스턴스 단위를 말하며 메세지를 받아 offset 지정 후 디스크에 저장합니다. 그리고 보통 3개 이상의 브로커로 구성하여 사용하는 것을 권장합니다.
- Cluster 내에서 각 1개씩 존재하는 Role Broker
  - Controller
    - 브로커를 모니터링 하고 장애가 발생한 경우 Follower 파티션 중 Leader 파티션을 재분배합니다.
  - Coordinator
    - 컨슈머 그룹을 모니터링하고 Rebalancing case가 발생한 경우 다른 컨슈머에게 매칭 해주는 역할을 수행합니다. (Rebalance)
    - Rebalancing Case
      - 컨슈머 그룹 내 새로운 컨슈머가 추가 되었을 때
      - 컨슈머 그룹 내 특정 컨슈머가 장애가 발생하였을 때
      - 토픽 내 새로운 파티션이 추가 되었을 때
    - (주의) 리밸런싱 중에는 컨슈머가 메세지를 읽을 수 없습니다.


만약 파티션이 1개이고 replication이 1인 topic이 존재하고 브로커가 3대라면 브로커 3대 중 1대에 해당 토픽의 정보(데이터)가 저장됩니다. (topic에 대한 좀더 자세한 내용은 밑에 있습니다.)

#### [Partition 1개 존재]
partition : 1, replication : 1  
![kafka_1_img](https://github.com/user-attachments/assets/ffa6d742-4005-4bc1-888b-04b4c9369fb8)

#### [Partition 2개 존재]
partition : 1, replication : 2  
![kafka_2_img](https://github.com/user-attachments/assets/2119ba60-70ac-4da7-bc75-d0871159fc83)

다만 Broker 개수에 따라서 replication 개수가 제한이 됩니다. Broker 개수가 3이면 replication 개수가 4가 될 수 없습니다.  
원본 1개의 Partition은 Leader Partition이라고 부릅니다.  나머지 Partition은 Follower Partition이라고 부릅니다.
이 Leader Partition과 Follower Partition을 합쳐서 ISR 즉 In Sync Replica라고 부릅니다.

Leader Partition의 장애가 발생할 수도 있기에 replication을 사용합니다. Leader Partition이 죽으면 Follower Partition 중 하나가 Leader Partition이 됩니다.
Producer가 Topic의 Partitoin에 데이터를 전달할 때 전달 받는 주체가 바로 Leader 파티션입니다. Producer에는 ack 라는 상세옵션이 있습니다.  ack는 0, 1, all 옵션 3개가 있습니다.


**[0]**  
Producer는 Leader Partition에 데이터를 전송하고 응답값을 받지 않습니다. 그래서 데이터가 정상적으로 전달 되었는지 보장이 되지 않습니다. ( 속도는 빠르지만 데이터 유실 가능성이 있다.)

**[1]**  
Leader Partition에 데이터를 전송하고 Leader 파티션이 데이터를 정상적으로 받았는지 응답값을 받습니다. 그러나 나머지 파티션에 복제되었는지 알 수 없습니다.

**[all]**  
1 옵션에 추가로 Follower Partition에 복제가 잘 이루어졌는지 응답값을 받습니다. Leader Partition에 데이터를 보낸 후 Follower Partition에도 데이터가 저장되는 것을 확인합니다. 
이 옵션은 데이터 유실이 없다고 보면 됩니다. (0, 1에 비해그러나 속도가 현저히 느립니다.)



** 3개 이상의 Broker를 사용할 때 Replication을 3으로 설정하는 것을 추천을 한다고 합니다.**

### Topic이란?
데이터가 들어가는 공간(파일시스템의 폴더와 유사)을 Topic 이라고 합니다. 카프카에서는 토픽을 여러개 생성할 수 있습니다. 이 토픽에 Producer 가 데이터를 넣고 Consumer 가 데이터를 가져가게 됩니다.
Topic 은 목적에 따라 click_log, send_sms 등 명확한 이름으로 표시를 해야 합니다.

![kafka_3_img](https://github.com/user-attachments/assets/60d9b423-45e1-43bb-b015-0a9c3d22b72e)

* 이제 Topic 내부에 대해서 알아보겠습니다.

**[Partition이 1개인 경우]**  
하나의 토픽은 여러개의 파티션으로 구성될 수 있으며 번호는 0번부터 시작합니다. Partition은 Queue와 같이 내부부터 쌓이게 되며 선입선출 하게 됩니다.
Consumer가 데이터를 가져가더라도 데이터는 삭제되지 않습니다.


새로운 Consumer가 같은 Partition에 붙었을 때 다시 0번 데이터부터 가져가서 사용할 수 있습니다. 다만 Consumer Group이 달라야 하고 auto.offset.reset=earliest여야 합니다. 
이처럼 사용할 경우 동일 데이터에 대해서 두번 사용할 수 있습니다. 이는 Kafka를 사용하는 이유이기도 합니다.  
![kafka_4_img](https://github.com/user-attachments/assets/0096d910-f052-4506-95eb-0e10e957ec88)  

**Consumer Group이란?**
- 하나의 Topic에 발행된 메세지를 여러 서비스가 컨슘하기 위해 그룹을 설정합니다.
  - 하나의 주문완료 메세지를 결제서비스에서도, 상품서비스에서도 컨슘합니다.
- 보통 소비 주체인 Application 단위로 Consumer Group을 생성, 관리합니다.
- 같은 Topic에 대한 소비주체를 늘리고 싶다면, 별도의 Consumer Group을 만들어 Topic을 구독합니다.

** 한 Partition은 두개의 Consumer에 붙을 수 없습니다. 한 Consumer는 여러개의 Partition에 붙을 수 있습니다.

![kafka_5_img](https://github.com/user-attachments/assets/aea11543-37d8-45d3-9e7a-a4c46ebe2057)  
*Partitioni의 개수가 그룹 내 Consumer 개수보다 많다면 잉여 Partition의 경우 메세지가 소비될 수 없음을 의미합니다.*

**[Partition이 2개 이상인 경우]**  
Producer가 데이터를 보낼 때 키를 지정할 수 있지만 만약 키가 null, 즉 키를 지정하지 않고 기본 설정을 사용한다면 round-robin으로 Partition이 지정됩니다.  
키가 있고 기본 파티셔너를 사용할 경우에는 키의 해시값을 구하고 특정 Partition에 항상 할당되게 됩니다.

예시에서는 키를 설정하지 않았으므로 새로운 데이터가 partition #1로 들어갑니다. 이후에는 round-robin으로 나눠져서 들어갑니다.
![kafka_6_img](https://github.com/user-attachments/assets/6f48d1c6-5653-4415-8f01-0f3cdf2c9312)

Partition을 늘리는 것은 아주! 조심해야합니다. Partition을 늘릴 수는 있지만 줄일 수는 없기 때문입니다. 그러면 Partition을 늘리는 이유는 무엇일까요? Partition을 늘리면 Consumer의 개수를 늘려서 데이터 처리를 분산시킬 수 있습니다.


그러면 Partition의 데이터는 언제 삭제 될까요? 삭제되는 타이밍은 옵션에 따라 다릅니다.


log.retentions.ms : 최대 record 보존 시간
log.retention.byte : 최대 record 보존 크기 (byte)


이를 지정하게 되면 record가 저장되는 최대 시간과 크기를 지정할 수 있습니다. 이를 지정하게 되면 적절하게 데이터가 삭제될 수 있도록 설정할 수 있습니다.